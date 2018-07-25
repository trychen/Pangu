package cn.mccraft.pangu.core.block;

import cn.mccraft.pangu.core.block.PGBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PGBlockRotatedPillar extends PGBlock {

    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public PGBlockRotatedPillar(Material materialIn) {
        super(materialIn);
        setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
    }

    @Override
    public boolean rotateBlock(net.minecraft.world.World world, @Nonnull BlockPos pos, @Nonnull EnumFacing axis) {
        net.minecraft.block.state.IBlockState state = world.getBlockState(pos);
        for (net.minecraft.block.properties.IProperty<?> prop : state.getProperties().keySet())
            if (prop.getName().equals("axis")) {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        return false;
    }

    @Nonnull
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:

                switch (state.getValue(AXIS)) {
                    case X:
                        return state.withProperty(AXIS, EnumFacing.Axis.Z);
                    case Z:
                        return state.withProperty(AXIS, EnumFacing.Axis.X);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Y;
        int i = meta & 12;

        if (i == 4)
            enumfacing$axis = EnumFacing.Axis.X;
        else if (i == 8)
            enumfacing$axis = EnumFacing.Axis.Z;

        return this.getDefaultState().withProperty(AXIS, enumfacing$axis);
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        EnumFacing.Axis enumfacing$axis = state.getValue(AXIS);

        if (enumfacing$axis == EnumFacing.Axis.X)
            i |= 4;
        else if (enumfacing$axis == EnumFacing.Axis.Z)
            i |= 8;

        return i;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS);
    }

    @Nonnull
    protected ItemStack getSilkTouchDrop(@Nonnull IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this));
    }

    @Nonnull
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(AXIS, facing.getAxis());
    }
}
