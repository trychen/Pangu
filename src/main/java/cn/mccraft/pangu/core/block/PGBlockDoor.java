package cn.mccraft.pangu.core.block;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PGBlockDoor extends BlockDoor {
    private boolean interactive = true;

    public PGBlockDoor(Material materialIn) {
        super(materialIn);
    }

    public boolean isInteractive() {
        return interactive;
    }

    public PGBlockDoor setInteractive(boolean interactive) {
        this.interactive = interactive;
        return this;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (isInteractive()) {
            return false; //Allow items to interact with the door
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
