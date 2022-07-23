package com.aizistral.nochatreports.mixins.client;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.mojang.patchy.MojangBlockListSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(MojangBlockListSupplier.class)
public class MixinMojangBlockListSupplier {

    /**
     * @reason Ignore the Mojang server blocklist
     */

    @Inject(method = "createBlockList", at = @At("HEAD"), cancellable = true, remap = false)
    private void createBlockList(CallbackInfoReturnable<Predicate<String>> info){
        if(NoReportsConfig.disableServerBlocklist()) {
            info.setReturnValue(null);
            info.cancel(); // don't bother downloading it in the first place
        }
    }
}
