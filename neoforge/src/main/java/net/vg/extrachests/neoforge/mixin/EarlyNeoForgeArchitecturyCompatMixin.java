package net.vg.extrachests.neoforge.mixin;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "dev.architectury.event.forge.EventHandlerImplCommon", remap = false)
abstract class EarlyNeoForgeArchitecturyCompatMixin {
}
