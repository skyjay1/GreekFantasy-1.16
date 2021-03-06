package greekfantasy.deity.favor;

import java.util.HashMap;
import java.util.Map;

import greekfantasy.GreekFantasy;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class Favor implements IFavor {
  
  protected final Map<ResourceLocation, FavorLevel> favorMap = new HashMap<>();
  private boolean enabled = true;
  private long effectTimestamp;
  private long effectCooldown = 1000;  
  private long triggeredTimestamp;
  private long triggeredCooldown = 100;
  
  public Favor() { }

  @Override
  public FavorLevel getFavor(final ResourceLocation deity) {
    if(favorMap.containsKey(deity)) {
      return favorMap.get(deity);
    } else {
      final FavorLevel favorLevel = new FavorLevel(0);
      favorMap.put(deity, favorLevel);
      return favorLevel;
    }
  }

  @Override
  public void setFavor(ResourceLocation deity, FavorLevel favorLevel) { favorMap.put(deity, favorLevel); }
  @Override
  public Map<ResourceLocation, FavorLevel> getAllFavor() { return favorMap; }
  @Override
  public boolean isEnabled() { return GreekFantasy.CONFIG.isFavorEnabled() && enabled; }
  @Override
  public void setEnabled(boolean enabledIn) { enabled = enabledIn; }
  @Override
  public long getEffectTimestamp() { return effectTimestamp; }
  @Override
  public void setEffectTimestamp(long timestamp) { this.effectTimestamp = timestamp; }
  @Override
  public long getEffectCooldown() { return effectCooldown; }
  @Override
  public void setEffectCooldown(long cooldown) { this.effectCooldown = cooldown; }
  @Override
  public long getTriggeredTimestamp() { return triggeredTimestamp; }
  @Override
  public void setTriggeredTimestamp(long timestamp) { this.triggeredTimestamp = timestamp; }
  @Override
  public long getTriggeredCooldown() { return triggeredCooldown; }
  @Override
  public void setTriggeredCooldown(long cooldown) { this.triggeredCooldown = cooldown; }
  
  @Override
  public String toString() {
    final StringBuilder b = new StringBuilder("Favor:");
    b.append(" enabled[").append(enabled).append("]");
    b.append(" effectTimestamp[").append(effectTimestamp).append("]");
    b.append(" effectCooldown[").append(effectCooldown).append("]");
    b.append(" triggeredTimestamp[").append(triggeredTimestamp).append("]");
    b.append(" triggeredCooldown[").append(triggeredCooldown).append("]");
    b.append("\nfavorMap[").append(getAllFavor().toString()).append("]");
    return b.toString();
  }
  
  public static class Storage implements IStorage<IFavor> {
    
    @Override
    public INBT writeNBT(Capability<IFavor> capability, IFavor instance, Direction side) {
      return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IFavor> capability, IFavor instance, Direction side, INBT nbt) {
      if(nbt instanceof CompoundNBT) {
        instance.deserializeNBT((CompoundNBT) nbt);
      } else {
        GreekFantasy.LOGGER.error("Failed to read Favor capability from NBT of type " + (nbt != null ? nbt.getType().getName() : "null"));
      }
    }
  }
  
  public static class Provider implements ICapabilitySerializable<CompoundNBT> {
    public IFavor instance = GreekFantasy.FAVOR.getDefaultInstance();

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      return cap == GreekFantasy.FAVOR ? GreekFantasy.FAVOR.orEmpty(cap, LazyOptional.of(() -> instance)) : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
      return (CompoundNBT) GreekFantasy.FAVOR.getStorage().writeNBT(GreekFantasy.FAVOR, this.instance, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
      GreekFantasy.FAVOR.getStorage().readNBT(GreekFantasy.FAVOR, this.instance, null, nbt);
    }
  }
}
