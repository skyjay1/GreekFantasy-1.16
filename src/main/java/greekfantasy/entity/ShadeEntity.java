package greekfantasy.entity;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class ShadeEntity extends CreatureEntity {
  
  public static final DataParameter<Integer> DATA_XP = EntityDataManager.createKey(ShadeEntity.class, DataSerializers.VARINT);
  private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(ShadeEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

  public static final String KEY_XP = "StoredXP";
  public static final String KEY_OWNER = "Owner";

  public ShadeEntity(final EntityType<? extends ShadeEntity> type, final World worldIn) {
    super(type, worldIn);
  }
  
  public static AttributeModifierMap.MutableAttribute getAttributes() {
    return MobEntity.func_233666_p_()
        .createMutableAttribute(Attributes.MAX_HEALTH, 12.0D)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.16D)
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.9D)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0.1D);
  }
  
  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(0, new SwimGoal(this));
    this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 12.0F));
    this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
  }
  
  @Override
  protected void registerData() {
    super.registerData();
    this.getDataManager().register(DATA_XP, Integer.valueOf(0));
    this.getDataManager().register(OWNER_UNIQUE_ID, Optional.empty());
  }
  
  @Override
  public void livingTick() {
    super.livingTick();
    // spawn particles
    if (world.isRemote()) {
      final double motion = 0.08D;
      final double radius = 1.2D;
      for (int i = 0; i < 5; i++) {
        world.addParticle(ParticleTypes.SMOKE, 
            this.getPosX() + (world.rand.nextDouble() - 0.5D) * radius, 
            this.getPosY() + 0.75D + (world.rand.nextDouble() - 0.5D) * radius * 0.75D, 
            this.getPosZ() + (world.rand.nextDouble() - 0.5D) * radius,
            (world.rand.nextDouble() - 0.5D) * motion, 
            (world.rand.nextDouble() - 0.5D) * motion * 0.5D,
            (world.rand.nextDouble() - 0.5D) * motion);
      }
    }
  }

  @Override
  public boolean attackEntityAsMob(final Entity entity) {
    if (super.attackEntityAsMob(entity)) {
      // remove XP or give wither effect
      if(entity instanceof PlayerEntity) {
        final PlayerEntity player = (PlayerEntity)entity;
        if(player.experienceTotal > 0) {
          // steal XP from player
          final int xpSteal = Math.min(player.experienceTotal, 10);
          player.giveExperiencePoints(-xpSteal);
          this.setStoredXP(this.getStoredXP() + xpSteal);
        } else {
          // brief wither effect
          player.addPotionEffect(new EffectInstance(Effects.WITHER, 60));
        }
      }
      return true;
    }
    return false;
  }
  
  @Override
  public boolean isInvulnerableTo(final DamageSource source) {
    return super.isInvulnerableTo(source) 
        || (source.getImmediateSource() instanceof PlayerEntity && this.isInvulnerableToPlayer((PlayerEntity)source.getImmediateSource()));
  }
  
  @Override
  public boolean canAttack(final EntityType<?> typeIn) {
    return typeIn == EntityType.PLAYER;
  }

  @Override
  public CreatureAttribute getCreatureAttribute() {
    return CreatureAttribute.UNDEAD;
  }
  
  @Override
  public boolean canDespawn(final double disToPlayer) {
    return false;
  }

  @Override
  public boolean onLivingFall(float distance, float damageMultiplier) {
    return false;
  }

  @Override
  protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
  }
  
  @Override
  public float getBrightness() {
    return 1.0F;
  }

  @Override
  public void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putInt(KEY_XP, this.getStoredXP());
    if (this.getOwnerUniqueId() != null) {
      compound.putUniqueId(KEY_OWNER, this.getOwnerUniqueId());
   }
  }

  @Override
  public void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.setStoredXP(compound.getInt(KEY_XP));
    if (compound.hasUniqueId(KEY_OWNER)) {
       this.setOwnerUniqueId(compound.getUniqueId(KEY_OWNER));
    }
  }

  @Override
  protected int getExperiencePoints(final PlayerEntity attackingPlayer) {
    return getStoredXP();
  }
  
  @Override
  public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
    if(this.getStoredXP() == 0) {
      this.setStoredXP(5 + this.rand.nextInt(10));
    }
    return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
 }

  public int getStoredXP() {
    return this.getDataManager().get(DATA_XP).intValue();
  }

  public void setStoredXP(int xp) {
    this.getDataManager().set(DATA_XP, xp);
  }
  
  @Nullable
  public UUID getOwnerUniqueId() {
     return this.dataManager.get(OWNER_UNIQUE_ID).orElse((UUID)null);
  }

  public void setOwnerUniqueId(@Nullable UUID uniqueId) {
     this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(uniqueId));
  }
  
  public boolean isInvulnerableToPlayer(final PlayerEntity player) {
    final UUID uuid = getOwnerUniqueId();
    return uuid != null && !uuid.equals(player.getUniqueID());
  }
  
}
