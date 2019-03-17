package cn.mccraft.pangu.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mouse
 * @since 1.0.0.5
 */
public class MultiBlockChecker {

    private Rule[][][] data;

    private int widthX;
    private int widthZ;
    private int height;

    private int offsetX = 0;
    private int offsetZ = 0;
    private int offsetY = 0;

    private MultiBlockChecker() {}

    /**
     * 构造一个方块结构检查者 例：
     *  new BlocksChecker(new String[][]{{"AAA", "AAA", "AAA"},
     * {"AAA", "ABA", "AAA"}, {"AAA", "AAA", "AAA"}}, "A", new
     * Rule(Blocks.stone,0},"B",new Rule(Blocks.air,0});
     *
     * 构造一个中空3X3X3石头结构
     *
     * @param data 输入一个二维String数组，分别为Y，Z，X
     * @param args 输入每个字符所代表的BlockRule
     */
    public MultiBlockChecker(String[][] data, Object... args) {
        this.setRule(data, args);
    }

    public void setRule(String[][] blocks, Object... args) {
        this.height = blocks.length;
        this.widthX = blocks[0].length;
        this.widthZ = blocks[0][0].length();

        Map<Character, Rule> target = new HashMap<Character, Rule>();
        Character key = null;
        for (Object arg : args) {
            if (key == null)
                key = (Character) arg;
            else {
                target.put(key, (Rule) arg);
                key = null;
            }
        }

        this.data = new Rule[this.height][this.widthX][this.widthZ];
        for (int y = 0; y < this.height; y++) {
            String[] data = blocks[y];
            for (int z = 0; z < this.widthZ; z++) {
                char[] array = data[z].toCharArray();
                for (int x = 0; x < this.widthX; x++) {
                    char character = array[x];
                    if (character == ' ')
                        continue;

                    this.data[y][z][x] = target.get(character);
                }
            }
        }
    }

    /**
     * 设置方块结构检查者偏移量
     *
     * @return 方块结构检查者
     */
    public MultiBlockChecker setOffset(int x, int y, int z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;

        return this;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public int getOffsetZ() {
        return this.offsetZ;
    }

    /**
     * 检查该多方块结构 请输入结构左上角的坐标
     *
     * @return 如果为真，则该结构是指定结构
     */
    public boolean check(IBlockAccess world, BlockPos pos) {
        int X = pos.getX() + this.offsetX;
        int Y = pos.getY() + this.offsetY;
        int Z = pos.getZ() + this.offsetZ;

        for (int y = 0; y < this.height; y++) {
            for (int z = 0; z < this.widthZ; z++) {
                for (int x = 0; x < this.widthX; x++) {
                    Rule rule = this.data[y][z][x];
                    if (rule != null && !rule.check(world, new BlockPos(X + x, Y - y, Z + z)))
                        return false;
                }
            }
        }
        return true;
    }

    public MultiBlockChecker copy() {
        MultiBlockChecker object = new MultiBlockChecker();
        object.data = this.data;
        return object;
    }

    public Rule[][][] getBlockRule() {
        return data;
    }

    public int getHeight() {
        return height;
    }

    public int getWidthX() {
        return widthX;
    }

    public int getWidthZ() {
        return widthZ;
    }


    public class Rule {

        private Block block;
        private int data;

        public Rule(Block block) {
            this.setRule(block, -1);
        }

        public Rule(Block block, int data) {
            this.setRule(block, data);
        }

        public Block getBlock() {
            return this.block;
        }

        public int getData() {
            return this.data;
        }

        public void setRule(Block block, int data) {
            this.block = block;
            this.data = data;
        }

        public boolean check(IBlockAccess world, BlockPos pos) {
            IBlockState state = world.getBlockState(pos);
            return this.check(state.getBlock(), state.getBlock().getMetaFromState(state));
        }

        public boolean check(@Nullable Block block, int data) {
            if (block == null) return false;

            if (!block.equals(this.block)) return false;

            return this.data == -1 || data == this.data;
        }

        public Rule copy() {
            return new Rule(this.block, this.data);
        }
    }
}
