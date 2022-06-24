package com.aizistral.nochatreports.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraftforge.internal.BrandingControl;
import net.minecraftforge.versions.mcp.MCPVersion;

@Mixin(value = BrandingControl.class, remap = false)
public class MixinBrandingControl {

	@Redirect(method = "computeBranding", at = @At(value = "INVOKE", target =
			"Lnet/minecraftforge/versions/mcp/MCPVersion;getMCVersion()Ljava/lang/String;"))
	private static String onGetMCVersion() {
		return MCPVersion.getMCVersion().equals("1.19.1") ? "1.19.84" : MCPVersion.getMCPVersion();
	}

}
