package cn.mccraft.pangu.core.util;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NonNullList<E> extends AbstractList<E> {
    private final List<E> delegate;
    private final E defaultElement;

    protected NonNullList() {
        this(new ArrayList(), null);
    }

    protected NonNullList(List<E> delegateIn, @Nullable E listType) {
        this.delegate = delegateIn;
        this.defaultElement = listType;
    }

    public static <E> NonNullList<E> create() {
        return new NonNullList<E>();
    }

    /**
     * Creates a new NonNullList with <i>fixed</i> size, and filled with the object passed.
     */
    public static <E> NonNullList<E> withSize(int size, E fill) {
        Validate.notNull(fill);
        Object[] aobject = new Object[size];
        Arrays.fill(aobject, fill);
        return new NonNullList<>(Arrays.asList((E[]) aobject), fill);
    }

    public static <E> NonNullList<E> from(E defaultElementIn, E... elements) {
        return new NonNullList<E>(Arrays.asList(elements), defaultElementIn);
    }

    @Nonnull
    public E get(int index) {
        return this.delegate.get(index);
    }

    public E set(int index, E element) {
        Validate.notNull(element);
        return this.delegate.set(index, element);
    }

    public void add(int p_add_1_, E p_add_2_) {
        Validate.notNull(p_add_2_);
        this.delegate.add(p_add_1_, p_add_2_);
    }

    public E remove(int index) {
        return this.delegate.remove(index);
    }


    public boolean remove(Object element) {
        return this.delegate.remove(element);
    }

    public int size() {
        return this.delegate.size();
    }

    public void clear() {
        if (this.defaultElement == null) {
            super.clear();
        } else {
            for (int i = 0; i < this.size(); ++i) {
                this.set(i, this.defaultElement);
            }
        }
    }
}