package immc.liasbetteroffhand.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.multiplayer.MultiPlayerGameMode.class)
public class OffhandUseMixin {

    private boolean blockOffhandUse = false; // Tracks whether the offhand should be blocked or not
	private Item lastMainHandItem = null; // Stores what item was in the main hand last tick
	private int lastMainHandCount = 0; // Stores how many of that item were in the stack last tick
	private boolean crossbowLoadedLastTick = false; // Stores whether the crossbow was loaded last tick

	// Check if we need to block the offhand this tick
    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    private void onTick(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance(); // Get Minecraft game instance
        if (mc.player == null) return; // DOn't do anything unless there's a player loaded

        Player player = mc.player; // Get reference to the local player
        boolean rightMouseHeld = mc.options.keyUse.isDown(); // Check if the right mouse button, or equivalent keybind, is being held down
        
		// Is the player currently using an item such as eating food, holding up a shield, drawing a bow, charging a spear, etc?
		// And are they doing so with their main hand?
		// True if both are satisfied.
		// Essentially, is the player currently using a hold right click behaviour with an item in their main hand?
		boolean isUsingMainHand = player.isUsingItem()
            && player.getUsedItemHand() == InteractionHand.MAIN_HAND;

		// Read the current main hand item and stack size this tick.
		ItemStack mainHandStack = player.getMainHandItem();
		Item currentItem = mainHandStack.isEmpty() ? null : mainHandStack.getItem(); // If the hand is empty store null
		int currentCount = mainHandStack.getCount();

		// Is the player currently using an instant use item such as a firework, snowball, egg, ender pearl, etc?
		// And was there something in their main hand last tick?
		// And has either the item in the main hand changes, or the stack gotten smaller?
		// True if all three are satisified.
		// Essentially, has the player used an instant use item and not let go of their right mouse button?
		boolean instantUseDetected = rightMouseHeld
			&& lastMainHandItem != null
			&& (currentItem != lastMainHandItem || currentCount < lastMainHandCount);
		
        if (isUsingMainHand || instantUseDetected) { // If either type of main hand use was detected, block the offhand
            blockOffhandUse = true;
        } else if (!rightMouseHeld) { // Otherwise, as long as the right mouse button is not currently being pressed, clear the flag
            blockOffhandUse = false;
			// This ensures if the player keeps the right mouse button held after completing their main hand use, it does not activate the offhand use.
        }

		// Save this tick's item and count
		lastMainHandItem = currentItem;
		lastMainHandCount = currentCount;
		
		boolean currentCrossbowLoaded = false; // Set the crossbow loaded as false by default
		if (!mainHandStack.isEmpty() && mainHandStack.getItem() instanceof CrossbowItem) { // Check if the mainhand isn't empty and the item being held is a crossbow
			currentCrossbowLoaded = CrossbowItem.isCharged(mainHandStack); // If the crossbow is loaded, set currentCrossbowLoaded to true
		}

		// Was the crossbow's state in this tick the same as the last?
		// And is the crossbow currently unloaded
		// And is the right mouse button currently being held
		// True if all three are satisfied.
		// Essentially, has the crossbow been fired this tick, and is the player still holding the right mouse button?
		boolean crossbowJustFired = crossbowLoadedLastTick 
			&& !currentCrossbowLoaded
			&& rightMouseHeld;
		
		if (crossbowJustFired) { // If the crossbow has just been fired, block the offhand
			blockOffhandUse = true;
		}

		crossbowLoadedLastTick = currentCrossbowLoaded; // Save this tick's crossbow state
    }

	// Tell Minecraft we need to block the offhand while consuming if necessary
    @Inject(
        method = "useItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void preventOffhandWhileConsuming(
        Player player,
        InteractionHand hand,
        CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (hand != InteractionHand.OFF_HAND) return; // If the hand that triggered this isn't the offhand, don't cancel.

        if (blockOffhandUse) { // If we need to block the offhand, pass this to Minecraft
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

	// Tell Minecraft we need to block the offhand while using the main hand if necessary
	@Inject(
		method = "useItemOn",
		at = @At("HEAD"),
		cancellable = true
	)
	private void preventOffhandBlockUseWhileUsingMainHand(
		LocalPlayer player,
		InteractionHand hand,
		BlockHitResult hitResult,
		CallbackInfoReturnable<InteractionResult> cir
	) {
		if (hand != InteractionHand.OFF_HAND) return; // If the hand that triggered this isn't the offhand, don't cancel.

		if (blockOffhandUse) { // If we need to block the offhand, pass this to Minecraft
			cir.setReturnValue(InteractionResult.PASS);
		}
	}
}