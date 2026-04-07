package me.av306.chathook.mixin;

import me.av306.chathook.ChatHook;
import me.av306.chathook.webhook.WebhookSystem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class DeathMixin {
    // Player death messages
    @Inject(method= "die", at=@At("HEAD"))
    private void died(DamageSource damageSource, CallbackInfo ci){
        ServerPlayer player = (ServerPlayer) (Object) this;
        ChatHook chatHook = ChatHook.getInstance();
        if ( chatHook.cm.getBoolConfig("log_game_messages") && chatHook.cm.getBoolConfig("enabled") )
            WebhookSystem.INSTANCE.sendMessage( player, "**" + player.getCombatTracker().getDeathMessage().getString() + "**");
    }
}