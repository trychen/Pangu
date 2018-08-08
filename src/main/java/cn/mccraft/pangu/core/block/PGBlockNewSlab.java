package cn.mccraft.pangu.core.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class PGBlockNewSlab extends PGBlock {
    public static final PropertyEnum<EnumBlockSlab> SLAB_TYPE = PropertyEnum.create("type", EnumBlockSlab.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public PGBlockNewSlab(Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }

    public PGBlockNewSlab(Material material, MapColor mapColor) {
        super(material, mapColor);
        this.setDefaultState(getDefaultState().withProperty(SLAB_TYPE, EnumBlockSlab.BOTTOM));
        this.setLightOpacity(255);

    }

    protected boolean canSilkHarvest() {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return this.isDouble(state) ? FULL_BLOCK_AABB : (state.getValue(SLAB_TYPE) == EnumBlockSlab.TOP ? AABB_TOP_HALF : AABB_BOTTOM_HALF);
    }

    public boolean isFullyOpaque(IBlockState state) {
        return isDouble(state) || state.getValue(SLAB_TYPE) == EnumBlockSlab.TOP;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return isDouble(state);
    }

    @Override
    public boolean getUseNeighborBrightness(IBlockState state) {
        return !isDouble(state);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return this.isDouble(state);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (net.minecraftforge.common.ForgeModContainer.disableStairSlabCulling)
            return super.doesSideBlockRendering(state, world, pos, face);

        if (state.isOpaqueCube())
            return true;

        EnumBlockSlab side = state.getValue(SLAB_TYPE);
        return (side == EnumBlockSlab.TOP && face == EnumFacing.UP) || (side == EnumBlockSlab.BOTTOM && face == EnumFacing.DOWN);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(SLAB_TYPE, EnumBlockSlab.BOTTOM);
        return isDouble(iblockstate) ? iblockstate.withProperty(SLAB_TYPE, EnumBlockSlab.DOUBLE) : (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double) hitY <= 0.5D) ? iblockstate : iblockstate.withProperty(SLAB_TYPE, EnumBlockSlab.TOP));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SLAB_TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(SLAB_TYPE, meta>= EnumBlockSlab.values().length? EnumBlockSlab.BOTTOM: EnumBlockSlab.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(SLAB_TYPE).ordinal();
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return this.isDouble(state) ? 2 : 1;
    }

    public boolean isFullCube(IBlockState state) {
        return this.isDouble(state);
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (this.isDouble(blockState)) {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        } else if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(blockState, blockAccess, pos, side)) {
            return false;
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    public boolean isDouble(IBlockState state) {
        return state.getValue(SLAB_TYPE) == EnumBlockSlab.DOUBLE;
    }

    public enum EnumBlockSlab implements IStringSerializable {
        TOP("top"),
        BOTTOM("bottom"),
        DOUBLE("double");

        private final String name;

        EnumBlockSlab(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
