package greekfantasy;

import java.util.function.Supplier;

import greekfantasy.block.ConnectedPillarBlock;
import greekfantasy.block.NestBlock;
import greekfantasy.block.StatueBlock;
import greekfantasy.entity.AraEntity;
import greekfantasy.entity.CentaurEntity;
import greekfantasy.entity.CerastesEntity;
import greekfantasy.entity.CerberusEntity;
import greekfantasy.entity.CyclopesEntity;
import greekfantasy.entity.CyprianCentaurEntity;
import greekfantasy.entity.DryadEntity;
import greekfantasy.entity.EmpusaEntity;
import greekfantasy.entity.GiganteEntity;
import greekfantasy.entity.GorgonEntity;
import greekfantasy.entity.HarpyEntity;
import greekfantasy.entity.MinotaurEntity;
import greekfantasy.entity.NaiadEntity;
import greekfantasy.entity.OrthusEntity;
import greekfantasy.entity.SatyrEntity;
import greekfantasy.entity.ShadeEntity;
import greekfantasy.entity.SirenEntity;
import greekfantasy.entity.UnicornEntity;
import greekfantasy.gui.StatueContainer;
import greekfantasy.item.ClubItem;
import greekfantasy.item.PanfluteItem;
import greekfantasy.structure.GFStructures;
import greekfantasy.structure.HarpyNestStructure;
import greekfantasy.structure.feature.HarpyNestFeature;
import greekfantasy.tileentity.StatueTileEntity;
import greekfantasy.util.StatuePose;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GreekFantasy.MODID)
public final class GFRegistry {

  //ENTITY TYPES

  public static EntityType<AraEntity> ARA_ENTITY;
  public static EntityType<CentaurEntity> CENTAUR_ENTITY;
  public static EntityType<CerastesEntity> CERASTES_ENTITY;
  public static EntityType<CerberusEntity> CERBERUS_ENTITY;
  public static EntityType<CyclopesEntity> CYCLOPES_ENTITY;
  public static EntityType<CyprianCentaurEntity> CYPRIAN_CENTAUR_ENTITY;
  public static EntityType<DryadEntity> DRYAD_ENTITY;
  public static EntityType<EmpusaEntity> EMPUSA_ENTITY;
  public static EntityType<GiganteEntity> GIGANTE_ENTITY;
  public static EntityType<GorgonEntity> GORGON_ENTITY;
  public static EntityType<HarpyEntity> HARPY_ENTITY;
  public static EntityType<MinotaurEntity> MINOTAUR_ENTITY;
  public static EntityType<NaiadEntity> NAIAD_ENTITY;
  public static EntityType<OrthusEntity> ORTHUS_ENTITY;
  public static EntityType<SatyrEntity> SATYR_ENTITY;
  public static EntityType<ShadeEntity> SHADE_ENTITY;
  public static EntityType<SirenEntity> SIREN_ENTITY;
  public static EntityType<UnicornEntity> UNICORN_ENTITY;

  // OBJECT HOLDERS //

  @ObjectHolder("panflute")
  public static final Item PANFLUTE = null;
  @ObjectHolder("stone_club")
  public static final Item STONE_CLUB = null;
  @ObjectHolder("wooden_club")
  public static final Item WOODEN_CLUB = null;
  
  @ObjectHolder("nest")
  public static final Block NEST_BLOCK = null;
  @ObjectHolder("limestone")
  public static final Block LIMESTONE = null;
  @ObjectHolder("limestone_stairs")
  public static final Block LIMESTONE_STAIRS = null;
  @ObjectHolder("polished_limestone")
  public static final Block POLISHED_LIMESTONE = null;
  @ObjectHolder("polished_limestone_stairs")
  public static final Block POLISHED_LIMESTONE_STAIRS = null;
  @ObjectHolder("marble")
  public static final Block MARBLE = null;
  @ObjectHolder("marble_stairs")
  public static final Block MARBLE_STAIRS = null;
  @ObjectHolder("polished_marble")
  public static final Block POLISHED_MARBLE = null; 
  @ObjectHolder("polished_marble_stairs")
  public static final Block POLISHED_MARBLE_STAIRS = null;
  @ObjectHolder("marble_pillar")
  public static final Block MARBLE_PILLAR = null;
  @ObjectHolder("limestone_statue")
  public static final Block LIMESTONE_STATUE = null;
  @ObjectHolder("marble_statue")
  public static final Block MARBLE_STATUE = null;
  
  @ObjectHolder("statue_te")
  public static final TileEntityType<StatueTileEntity> STATUE_TE = null;

  @ObjectHolder("statue_container")
  public static final ContainerType<StatueContainer> STATUE_CONTAINER = null;

  public static ItemGroup GREEK_GROUP = new ItemGroup("greekfantasy") {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(PANFLUTE);
    }
  };

  // REGISTRY METHODS //

  @SubscribeEvent
  public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
    GreekFantasy.LOGGER.info("registerEntities");
    ARA_ENTITY = registerEntityType(event, AraEntity::new, AraEntity::getAttributes, MonsterEntity::canMonsterSpawnInLight, "ara", 0.7F, 1.8F, EntityClassification.MONSTER, true);
    CENTAUR_ENTITY = registerEntityType(event, CentaurEntity::new, CentaurEntity::getAttributes, MobEntity::canSpawnOn, "centaur", 1.39F, 2.49F, EntityClassification.CREATURE, false);
    CYPRIAN_CENTAUR_ENTITY = registerEntityType(event, CyprianCentaurEntity::new, CyprianCentaurEntity::getAttributes, MobEntity::canSpawnOn, "cyprian", 1.39F, 2.49F, EntityClassification.MONSTER, false);
    CERASTES_ENTITY = registerEntityType(event, CerastesEntity::new, CerastesEntity::getAttributes, MobEntity::canSpawnOn, "cerastes", 0.98F, 0.94F, EntityClassification.CREATURE, false);
    CERBERUS_ENTITY = registerEntityType(event, CerberusEntity::new, CerberusEntity::getAttributes, MobEntity::canSpawnOn, "cerberus", 1.98F, 1.9F, EntityClassification.MONSTER, false);
    CYCLOPES_ENTITY = registerEntityType(event, CyclopesEntity::new, CyclopesEntity::getAttributes, MobEntity::canSpawnOn, "cyclopes", 1.19F, 2.79F, EntityClassification.MONSTER, false);
    DRYAD_ENTITY = registerEntityType(event, DryadEntity::new, DryadEntity::getAttributes, MobEntity::canSpawnOn, "dryad", 0.48F, 1.8F, EntityClassification.CREATURE, false);
    EMPUSA_ENTITY = registerEntityType(event, EmpusaEntity::new, EmpusaEntity::getAttributes, MonsterEntity::canMonsterSpawnInLight, "empusa", 0.7F, 1.8F, EntityClassification.MONSTER, true);
    GIGANTE_ENTITY = registerEntityType(event, GiganteEntity::new, GiganteEntity::getAttributes, MobEntity::canSpawnOn, "gigante", 1.19F, 2.79F, EntityClassification.CREATURE, false);
    GORGON_ENTITY = registerEntityType(event, GorgonEntity::new, GorgonEntity::getAttributes, MonsterEntity::canMonsterSpawn, "gorgon", 0.9F, 1.9F, EntityClassification.MONSTER, false);
    HARPY_ENTITY = registerEntityType(event, HarpyEntity::new, HarpyEntity::getAttributes, MobEntity::canSpawnOn, "harpy", 0.7F, 1.8F, EntityClassification.MONSTER, false);
    MINOTAUR_ENTITY = registerEntityType(event, MinotaurEntity::new, MinotaurEntity::getAttributes, MonsterEntity::canMonsterSpawnInLight, "minotaur", 0.7F, 1.8F, EntityClassification.MONSTER, false);
    NAIAD_ENTITY = registerEntityType(event, NaiadEntity::new, NaiadEntity::getAttributes, NaiadEntity::canNaiadSpawnOn, "naiad", 0.48F, 1.8F, EntityClassification.WATER_CREATURE, false);
    ORTHUS_ENTITY = registerEntityType(event, OrthusEntity::new, OrthusEntity::getAttributes, MobEntity::canSpawnOn, "orthus", 0.6F, 0.85F, EntityClassification.MONSTER, true);
    SATYR_ENTITY = registerEntityType(event, SatyrEntity::new, SatyrEntity::getAttributes, MobEntity::canSpawnOn, "satyr", 0.7F, 1.8F, EntityClassification.CREATURE, false);
    SHADE_ENTITY = registerEntityType(event, ShadeEntity::new, ShadeEntity::getAttributes, MonsterEntity::canMonsterSpawnInLight, "shade", 0.7F, 1.8F, EntityClassification.MONSTER, true);
    SIREN_ENTITY = registerEntityType(event, SirenEntity::new, SirenEntity::getAttributes, SirenEntity::canSirenSpawnOn, "siren", 0.6F, 1.9F, EntityClassification.WATER_CREATURE, false);
    UNICORN_ENTITY = registerEntityType(event, UnicornEntity::new, UnicornEntity::getAttributes, MobEntity::canSpawnOn, "unicorn", 1.39F, 1.98F, EntityClassification.CREATURE, false);
  }
  
  @SubscribeEvent
  public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
    GreekFantasy.LOGGER.info("registerTileEntities");
    event.getRegistry().register(
        TileEntityType.Builder.create(StatueTileEntity::new, LIMESTONE_STATUE, MARBLE_STATUE)
        .build(null).setRegistryName(GreekFantasy.MODID, "statue_te")
    );
  }
  
  @SubscribeEvent
  public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
    GreekFantasy.LOGGER.info("registerContainers");
    ContainerType<StatueContainer> containerType = IForgeContainerType.create((windowId, inv, data) -> {
      final boolean isFemale = data.readBoolean();
      final BlockPos blockpos = data.readBlockPos();
      final CompoundNBT poseTag = data.readCompoundTag();
      final String name = data.readString();
      final StatuePose pose = new StatuePose(poseTag);
      return new StatueContainer(windowId, inv, new Inventory(2), pose, isFemale, name, blockpos);
    });
    event.getRegistry().register(containerType.setRegistryName(GreekFantasy.MODID, "statue_container"));
  }

  @SubscribeEvent
  public static void registerItems(final RegistryEvent.Register<Item> event) {
    GreekFantasy.LOGGER.info("registerItems");
    // items
    event.getRegistry().registerAll(
        new PanfluteItem(new Item.Properties().group(GREEK_GROUP).maxDamage(100))
          .setRegistryName(GreekFantasy.MODID, "panflute"),
        new ClubItem(ItemTier.IRON, new Item.Properties().group(GREEK_GROUP))
          .setRegistryName(GreekFantasy.MODID, "iron_club"),
        new ClubItem(ItemTier.STONE, new Item.Properties().group(GREEK_GROUP))
          .setRegistryName(GreekFantasy.MODID, "stone_club"),
        new ClubItem(ItemTier.WOOD, new Item.Properties().group(GREEK_GROUP))
          .setRegistryName(GreekFantasy.MODID, "wooden_club")
    );
    // block items
    registerItemBlock(event, NEST_BLOCK, "nest");
    
    registerItemBlock(event, MARBLE, "marble");
    registerItemBlock(event, MARBLE_STAIRS, "marble_stairs");
    registerItemBlock(event, POLISHED_MARBLE, "polished_marble");
    registerItemBlock(event, POLISHED_MARBLE_STAIRS, "polished_marble_stairs");
    registerItemBlock(event, MARBLE_PILLAR, "marble_pillar");

    registerItemBlock(event, LIMESTONE, "limestone");
    registerItemBlock(event, LIMESTONE_STAIRS, "limestone_stairs");
    registerItemBlock(event, POLISHED_LIMESTONE, "polished_limestone");
    registerItemBlock(event, POLISHED_LIMESTONE_STAIRS, "polished_limestone_stairs");
    
    registerItemBlock(event, LIMESTONE_STATUE, "limestone_statue");
    registerItemBlock(event, MARBLE_STATUE, "marble_statue");
    
  }

  @SubscribeEvent
  public static void registerBlocks(final RegistryEvent.Register<Block> event) {
    GreekFantasy.LOGGER.info("registerBlocks");
    
    registerBlockPolishedAndStairs(event, Block.Properties.from(Blocks.STONE), "limestone");
    registerBlockPolishedAndStairs(event, Block.Properties.from(Blocks.DIORITE), "marble");

    event.getRegistry().registerAll(
        new NestBlock(Block.Properties.from(Blocks.HAY_BLOCK).notSolid().variableOpacity())
          .setRegistryName(GreekFantasy.MODID, "nest"),
        new ConnectedPillarBlock(Block.Properties.from(Blocks.STONE).notSolid())
          .setRegistryName(GreekFantasy.MODID, "marble_pillar"),
        new StatueBlock(StatueBlock.StatueMaterial.LIMESTONE)
          .setRegistryName(GreekFantasy.MODID, "limestone_statue"),
        new StatueBlock(StatueBlock.StatueMaterial.MARBLE)
          .setRegistryName(GreekFantasy.MODID, "marble_statue")
    );
  }

  @SubscribeEvent
  public static void registerStructures(final RegistryEvent.Register<Structure<?>> event) {
    GreekFantasy.LOGGER.info("registerStructures");
    // TODO
  }

  @SubscribeEvent
  public static void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
    GreekFantasy.LOGGER.info("registerFeatures");
    event.getRegistry().register(
        new HarpyNestFeature(NoFeatureConfig.field_236558_a_)
          .setRegistryName(GreekFantasy.MODID, HarpyNestStructure.NAME));
  }

  // OTHER SETUP METHODS //

  public static void addBiomeFeatures(final BiomeLoadingEvent event) {
    GreekFantasy.LOGGER.info("registerBiomeFeatures");
    if(event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.THEEND) {
      event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, 
          new ConfiguredFeature<>(GFStructures.HARPY_NEST_FEATURE, IFeatureConfig.NO_FEATURE_CONFIG)
          .withPlacement(new ConfiguredPlacement<>(Placement.field_242898_b, new ChanceConfig(4))));
    }
  }
  
  public static void addBiomeSpawns(final BiomeLoadingEvent event) {
    GreekFantasy.LOGGER.info("registerBiomeSpawns");
    final String name = event.getName().getPath();
    if(event.getCategory() == Biome.Category.NETHER) {
      // register nether spawns
      registerSpawns(event.getSpawns(), CERBERUS_ENTITY, 1, 1, 1);
      registerSpawns(event.getSpawns(), ORTHUS_ENTITY, 1, 1, 4);
    } else if(event.getCategory() != Biome.Category.THEEND) {
      // register overworld spawns
      // TODO conditions
      registerSpawns(event.getSpawns(), ARA_ENTITY, 10, 2, 5);
      registerSpawns(event.getSpawns(), CENTAUR_ENTITY, 10, 2, 4);
      // desert spawns
      if(event.getCategory() == Biome.Category.DESERT) {
        registerSpawns(event.getSpawns(), CERASTES_ENTITY, 12, 1, 1);
      }
      registerSpawns(event.getSpawns(), CYCLOPES_ENTITY, 10, 1, 3);
      registerSpawns(event.getSpawns(), CYPRIAN_CENTAUR_ENTITY, 10, 2, 3);
      // forest spawns
      if(name.contains("forest") || name.contains("taiga") || name.contains("jungle") || name.contains("savanna") || name.contains("wooded")) {
        registerSpawns(event.getSpawns(), DRYAD_ENTITY, 18, 1, 2);
        registerSpawns(event.getSpawns(), SATYR_ENTITY, 14, 2, 5);
      }
      registerSpawns(event.getSpawns(), EMPUSA_ENTITY, 50, 1, 3);
      if(event.getCategory() == Biome.Category.EXTREME_HILLS) {
        registerSpawns(event.getSpawns(), GIGANTE_ENTITY, 10, 1, 4);
      }
      registerSpawns(event.getSpawns(), GORGON_ENTITY, 10, 3, 3);
      registerSpawns(event.getSpawns(), HARPY_ENTITY, 10, 1, 3);
      registerSpawns(event.getSpawns(), MINOTAUR_ENTITY, 30, 2, 5);
      
      registerSpawns(event.getSpawns(), SHADE_ENTITY, 5, 1, 1);
      // ocean spawns
      if(event.getCategory() == Biome.Category.OCEAN) {
        registerSpawns(event.getSpawns(), SIREN_ENTITY, 10, 2, 5);
      }
      if(event.getCategory() == Biome.Category.OCEAN || event.getCategory() == Biome.Category.RIVER) {
        registerSpawns(event.getSpawns(), NAIAD_ENTITY, 8, 2, 5);
      }
      // plains spawns
      if(event.getCategory() == Biome.Category.PLAINS) {
        registerSpawns(event.getSpawns(), UNICORN_ENTITY, 8, 2, 5);
      }
    }
  }

  // HELPER METHODS //

  private static <T extends MobEntity> EntityType<T> registerEntityType(final RegistryEvent.Register<EntityType<?>> event,
      final IFactory<T> factoryIn, final Supplier<AttributeModifierMap.MutableAttribute> mapSupplier, 
      final EntitySpawnPlacementRegistry.IPlacementPredicate<T> placementPredicate, final String name,
      final float width, final float height, final EntityClassification classification, final boolean fireproof) {
    EntityType.Builder<T> entityTypeBuilder = EntityType.Builder.create(factoryIn, classification).size(width, height);
    if (fireproof) entityTypeBuilder.immuneToFire();
    EntityType<T> entityType = entityTypeBuilder.build(name);
    entityType.setRegistryName(GreekFantasy.MODID, name);
    event.getRegistry().register(entityType);
    GlobalEntityTypeAttributes.put(entityType, mapSupplier.get().create());
    
    EntitySpawnPlacementRegistry.register(entityType, classification == EntityClassification.WATER_CREATURE ? PlacementType.IN_WATER : PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, placementPredicate);
    return entityType;
  }
  
  private static MobSpawnInfo.Builder registerSpawns(final MobSpawnInfoBuilder builder, final EntityType<?> entity, final int weight, final int min, final int max) {
    return builder.withSpawner(entity.getClassification(), new MobSpawnInfo.Spawners(entity, weight, min, max));
  }
  
  private static void registerBlockPolishedAndStairs(final RegistryEvent.Register<Block> event, final Block.Properties properties, final String registryName) {
    final Block raw = new Block(properties).setRegistryName(GreekFantasy.MODID, registryName);
    final Block polished = new Block(properties).setRegistryName(GreekFantasy.MODID, "polished_" + registryName);
    event.getRegistry().register(raw);
    event.getRegistry().register(polished);
    event.getRegistry().register(new StairsBlock(() -> raw.getDefaultState(), properties).setRegistryName(GreekFantasy.MODID, registryName + "_stairs"));
    event.getRegistry().register(new StairsBlock(() -> polished.getDefaultState(), properties).setRegistryName(GreekFantasy.MODID, "polished_" + registryName + "_stairs"));
  }
  
  private static void registerItemBlock(final RegistryEvent.Register<Item> event, final Block block, final String registryName) {
    event.getRegistry().register(new BlockItem(block, new Item.Properties().group(GREEK_GROUP)).setRegistryName(GreekFantasy.MODID, registryName));
  }
}
