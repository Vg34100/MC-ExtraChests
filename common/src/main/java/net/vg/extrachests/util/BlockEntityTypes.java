package net.vg.extrachests.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.function.BiFunction;

public final class BlockEntityTypes {
    private static final String FACTORY_CLASS =
            "net.minecraft.world.level.block.entity.BlockEntityType$BlockEntitySupplier";

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> BlockEntityType<T> create(
            BiFunction<BlockPos, BlockState, T> factory, Block block) {
        try {
            Class<?> factoryClass = Class.forName(FACTORY_CLASS);
            Object factoryProxy = Proxy.newProxyInstance(
                    factoryClass.getClassLoader(), new Class<?>[]{factoryClass}, (proxy, method, args) -> {
                        if (method.getName().equals("create")) {
                            return factory.apply((BlockPos) args[0], (BlockState) args[1]);
                        }
                        if (method.getName().equals("toString")) return "ExtraChestsBlockEntityFactory";
                        if (method.getName().equals("hashCode")) return System.identityHashCode(proxy);
                        if (method.getName().equals("equals")) return proxy == args[0];
                        throw new UnsupportedOperationException(method.toString());
                    });
            Constructor<BlockEntityType> constructor =
                    BlockEntityType.class.getDeclaredConstructor(factoryClass, Set.class);
            constructor.setAccessible(true);
            return (BlockEntityType<T>) constructor.newInstance(factoryProxy, Set.of(block));
        } catch (ReflectiveOperationException error) {
            throw new IllegalStateException("Unable to construct BlockEntityType", error);
        }
    }

    private BlockEntityTypes() {}
}
