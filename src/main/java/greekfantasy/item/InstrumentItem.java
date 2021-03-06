package greekfantasy.item;

import greekfantasy.GreekFantasy;
import greekfantasy.gui.GuiLoader;
import greekfantasy.util.SongManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public abstract class InstrumentItem extends Item {
  
  public static final ResourceLocation DEFAULT_SONG = new ResourceLocation(GreekFantasy.MODID, "greensleeves");
  public static final String KEY_SONG = "Song";
  public static final String TOOLTIP = "item.tooltip.right_click_instrument";

  public InstrumentItem(final Properties properties) {
    super(properties);
  }
  
  @Override
  public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    // add the item to the group with enchantment already applied
    if (this.isInGroup(group)) {
      final ItemStack stack = new ItemStack(this);
      writeSong(stack, DEFAULT_SONG);
      items.add(stack);
    }
  }

  @Override
  public int getUseDuration(final ItemStack stack) {
    return 72000;
  }
  
  @Override
  public UseAction getUseAction(final ItemStack stack) {
    return UseAction.CROSSBOW;
  }

  @Override
  public void onUsingTick(final ItemStack stack, final LivingEntity player, final int count) {
    // note: count starts at #getUseDuration and decreases to zero
    SongManager.playMusic(player, this, readSong(stack), getUseDuration(stack) - count, getTrebleVolume(), getBassVolume());
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    // check if player is sneaking and open GUI
    if(playerIn.isSneaking()) {
      if(worldIn.isRemote()) {
        GuiLoader.openSongGui(playerIn, playerIn.inventory.currentItem, itemstack);
      }
    } else {
      playerIn.setActiveHand(handIn);
    }
    return ActionResult.resultConsume(itemstack);
  }
  
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
    int timeUsed = getUseDuration(stack) - timeLeft;
    // Check if player stopped using the item very soon after starting - if so, open song selection GUI instead
    if(timeUsed < 10 && stack.getItem() == this && worldIn.isRemote() && entityLiving instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity)entityLiving;
      GuiLoader.openSongGui(player, player.inventory.currentItem, stack);
    }
  }

  // Instrument //
  
  public static void writeSong(final ItemStack stack, final ResourceLocation song) {
    stack.getOrCreateTag().putString(KEY_SONG, song.toString());
  }
  
  public static ResourceLocation readSong(final ItemStack stack) {
    if(stack.getOrCreateTag().contains(KEY_SONG)) {
      return new ResourceLocation(stack.getTag().getString(KEY_SONG));
    }
    return DEFAULT_SONG;
  }
  
  public float getPitch(final int note) {
    return (float)Math.pow(2.0D, (double)(note - 12) / 12.0D);
  }
  
  public float getTrebleVolume() { 
    return 1.0F; 
  }
  
  public float getBassVolume() {
    return 0.45F;
  }
  
  public abstract SoundEvent getSound();
}
