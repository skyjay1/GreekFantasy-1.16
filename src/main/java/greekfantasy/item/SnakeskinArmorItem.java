package greekfantasy.item;

import greekfantasy.GFRegistry;
import greekfantasy.GreekFantasy;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SnakeskinArmorItem extends ArmorItem {
  protected static final IArmorMaterial MATERIAL = new SnakeskinArmorMaterial();
   
  private static final String TEXTURE_1 = GreekFantasy.MODID + ":textures/models/armor/snakeskin_layer_1.png";
  private static final String TEXTURE_2 = GreekFantasy.MODID + ":textures/models/armor/snakeskin_layer_2.png";


  public SnakeskinArmorItem(final EquipmentSlotType slot, Properties builderIn) {
    super(MATERIAL, slot, builderIn);
  }
  
  @Override
  public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    // add the item to the group with enchantment already applied
    if (this.isInGroup(group)) {
      final ItemStack stack = new ItemStack(this);
      if(GreekFantasy.CONFIG.isPoisonEnabled()) {
        stack.addEnchantment(GFRegistry.POISON_ENCHANTMENT, 1);
      }
      items.add(stack);
    }
  }
  
  @Override
  public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
    // add Poison enchantment if not present
    if(GreekFantasy.CONFIG.isPoisonEnabled() && EnchantmentHelper.getEnchantmentLevel(GFRegistry.POISON_ENCHANTMENT, stack) < 1) {
      stack.addEnchantment(GFRegistry.POISON_ENCHANTMENT, 1);
    }
  }

  @Override
  public boolean hasEffect(ItemStack stack) {
    return GreekFantasy.CONFIG.isPoisonEnabled() ? stack.getEnchantmentTagList().size() > 1 : super.hasEffect(stack);
  }

  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
   * update it's contents.
   */
  @Override
  public void inventoryTick(final ItemStack stack, final World worldIn, final Entity entityIn, 
      final int itemSlot, final boolean isSelected) {
    // add Poison enchantment if not present
    if(GreekFantasy.CONFIG.isPoisonEnabled() && EnchantmentHelper.getEnchantmentLevel(GFRegistry.POISON_ENCHANTMENT, stack) < 1) {
      stack.addEnchantment(GFRegistry.POISON_ENCHANTMENT, 1);
    }
  }

  /**
   * Called by RenderBiped and RenderPlayer to determine the armor texture that
   * should be use for the currently equipped item. This will only be called on
   * instances of ItemArmor.
   *
   * Returning null from this function will use the default value.
   *
   * @param stack  ItemStack for the equipped armor
   * @param entity The entity wearing the armor
   * @param slot   The slot the armor is in
   * @param type   The subtype, can be null or "overlay"
   * @return Path of texture to bind, or null to use default
   */
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
    return (slot == EquipmentSlotType.LEGS) ? TEXTURE_2 : TEXTURE_1;
  }
  
  public static class SnakeskinArmorMaterial implements IArmorMaterial {
    private static final String NAME = "snakeskin";
    @Override
    public int getDamageReductionAmount(EquipmentSlotType slot) { return ArmorMaterial.CHAIN.getDamageReductionAmount(slot); }
    @Override
    public int getDurability(EquipmentSlotType slot) { return ArmorMaterial.IRON.getDurability(slot); }
    @Override
    public int getEnchantability() { return ArmorMaterial.IRON.getEnchantability(); }
    @Override
    public float getKnockbackResistance() { return ArmorMaterial.LEATHER.getKnockbackResistance(); }
    @Override
    public String getName() { return NAME; }
    @Override
    public Ingredient getRepairMaterial() { return Ingredient.fromItems(GFRegistry.TOUGH_SNAKESKIN); }
    @Override
    public SoundEvent getSoundEvent() { return SoundEvents.ITEM_ARMOR_EQUIP_TURTLE; }
    @Override
    public float getToughness() { return ArmorMaterial.IRON.getToughness(); }
  }
}
