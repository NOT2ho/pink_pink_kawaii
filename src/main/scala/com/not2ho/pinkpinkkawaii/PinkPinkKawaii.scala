package com.not2ho.pinkpinkkawaii

import com.not2ho.pinkpinkkawaii.fluid.{PinkFluid, PinkLiquid}
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.{BlockItem, BucketItem, CreativeModeTab, CreativeModeTabs, Item, Items}
import net.minecraft.world.level.block.{Block, Blocks}
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraft.client.renderer.RenderType
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.{FMLClientSetupEvent, FMLCommonSetupEvent}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}
import org.apache.logging.log4j.LogManager
import net.minecraft.world.level.block.LiquidBlock


@Mod(PinkPinkKawaii.MOD_ID)
object PinkPinkKawaii {
  final val MOD_ID = "pink_pink_kawaii"

  private val LOGGER = LogManager.getLogger

  val BLOCKS: DeferredRegister[Block] = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID)
  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID)

  val PINK_LIQUID_BLOCK: RegistryObject[LiquidBlock] = BLOCKS.register("pink_liquid_block",
    () => new LiquidBlock(PinkLiquid.SOURCE_PINK_LIQUID, BlockBehaviour.Properties.copy(Blocks.WATER)));

  val PINK_LIQUID_BLOCK_ITEM: RegistryObject[Item] = ITEMS.register("pink_liquid_block"
    , () => new BlockItem(PINK_LIQUID_BLOCK.get(), new Item.Properties()))



  val PINK_GRASS_BLOCK: RegistryObject[Block] = BLOCKS.register("pink_grass_block", () => new Block(BlockBehaviour.Properties.of()
                                                                                          .destroyTime(2.0f)
                                                                                          .explosionResistance(0.5f)))
  
  val PINK_GRASS_BLOCK_ITEM: RegistryObject[Item] = ITEMS.register("pink_grass_block", () => new BlockItem(PINK_GRASS_BLOCK.get(), new Item.Properties()))

  val CREATIVE_MODE_TABS: DeferredRegister[CreativeModeTab] = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID)

  val PINK_LIQUID_BUCKET: RegistryObject[Item] = ITEMS.register("pink_liquid_bucket",
    () => new BucketItem(PinkLiquid.SOURCE_PINK_LIQUID, new Item.Properties().craftRemainder(Items.BUCKET)
      .stacksTo(1)))
  
  val PINK_TAB: RegistryObject[CreativeModeTab] = CREATIVE_MODE_TABS.register("pink_tab", () => CreativeModeTab.builder()
    .withTabsBefore(CreativeModeTabs.COMBAT)
    .icon(() => PINK_GRASS_BLOCK_ITEM.get().getDefaultInstance)
    .displayItems((parameters, output) => {
      output.accept(PINK_GRASS_BLOCK_ITEM.get())
    }).build()
  )

  {
    val modEventBus = FMLJavaModLoadingContext.get().getModEventBus

    modEventBus.addListener(this.commonSetup)

    BLOCKS.register(modEventBus)


    PinkLiquid.register(modEventBus)

    PinkFluid.register(modEventBus)

    ITEMS.register(modEventBus)
    CREATIVE_MODE_TABS.register(modEventBus)

    MinecraftForge.EVENT_BUS.register(this)

    modEventBus.addListener(this.addCreative)
    ModLoadingContext.get.registerConfig(ModConfig.Type.COMMON, Config.SPEC)
  }

  private def commonSetup(event: FMLCommonSetupEvent): Unit = {
  }

  private def addCreative(event: BuildCreativeModeTabContentsEvent): Unit = {
    if (event.getTabKey == CreativeModeTabs.BUILDING_BLOCKS)
      event.accept(PINK_GRASS_BLOCK)
  }

  @SubscribeEvent
  def onServerStarting(event: ServerStartingEvent): Unit = {
  }

  @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Array(Dist.CLIENT))
  object ClientModEvents {
    @SubscribeEvent
    def onClientSetup(event: FMLClientSetupEvent): Unit = {
      ItemBlockRenderTypes.setRenderLayer(PinkLiquid.SOURCE_PINK_LIQUID.get, RenderType.translucent)
      ItemBlockRenderTypes.setRenderLayer(PinkLiquid.FLOWING_PINK_LIQUID.get, RenderType.translucent)

    }
  }
}
