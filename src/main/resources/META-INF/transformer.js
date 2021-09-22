var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LdcInsnNode = Java.type('org.objectweb.asm.tree.LdcInsnNode');
var InvokeDynamicInsnNode = Java.type('org.objectweb.asm.tree.InvokeDynamicInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var FrameNode = Java.type('org.objectweb.asm.tree.FrameNode');
var LineNumberNode = Java.type('org.objectweb.asm.tree.LineNumberNode');

function initializeCoreMod() {

    return {
		// Forge's event doesn't include the slot involved in the crafting, so we do this ourselves
        'crafting_result_slot_patch': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.inventory.ResultSlot'
            },
            'transformer': function(classNode) {
                patchMethod([{
                    obfName: "m_5845_",
                    name: "checkTakeAchievements",
                    desc: "(Lnet/minecraft/world/item/ItemStack;)V",
                    patches: [patchCraftingResultSlotOnCrafting]
                }], classNode, "ResultSlot");
                return classNode;
            }
        }
    };
}

function patchMethod(entries, classNode, name) {

    log("Patching " + name + "...");
    for (var i = 0; i < entries.length; i++) {

        var entry = entries[i];
        var method = findMethod(classNode.methods, entry);
        var flag = !!method;
		debug((flag ? "Run" : "Dont Run"));
        if (flag) {

            var obfuscated = !method.name.equals(entry.name);
            for (var j = 0; j < entry.patches.length; j++) {
				var flag2 = true;
                var patch = entry.patches[j];
                if (!patchInstructions(method, patch.filter, patch.action, obfuscated)) {
					flag2 = false;
                    flag = false;
                }
				log((flag2 ? " Successfully applied" : " Failed to apply")+" patch " + (j+1));
            }
        }

        log("Patch for " + name + "#" + entry.name + (flag ? " was successful" : " failed"));
    }
}

function findMethod(methods, entry) {

    for (var i = 0; i < methods.length; i++) {

        var method = methods[i];
        if ((method.name.equals(entry.obfName) || method.name.equals(entry.name)) && method.desc.equals(entry.desc)) {

            return method;
        }
    }
}

function patchInstructions(method, filter, action, obfuscated) {

    var instructions = method.instructions.toArray();
    for (var i = 0; i < instructions.length; i++) {

        var node = filter(instructions[i], obfuscated);
		debug("DOES NODE MATCH: "+(!!node));
        if (!!node) {

            break;
        }
    }
	debug("APPLY PATCH ON NODE: "+(!!node));
    if (!!node) {
        action(node, method.instructions, obfuscated);
        return true;
    }
}

var patchCraftingResultSlotOnCrafting = {
    filter: function(node, obfuscated) {
		debug("");
		debug("*****************");
		if(node != null){
			if(!!node.owner) debug("Node Owner: "+node.owner);
			if(!!node.name) debug("Node Name: "+node.name);
			if(!!node.desc) debug("Node Desc: "+node.desc);
			if(!!node.var) debug("Node Var: "+node.var);	
			if(!!node.getOpcode()) debug("Node Opcodes: "+node.getOpcode());
		}
		debug("*****************");
		debug("");
        if (matchesHook(node, "net/minecraft/world/item/ItemStack", "m_41678_", "onCraftedBy", "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;I)V")) {
            return node;
        }
    },
    action: function(node, instructions, obfuscated) {
        var insnList = new InsnList();
		insnList.add(new VarInsnNode(Opcodes.ALOAD, null));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/inventory/ResultSlot", obfuscated ? "f_40162_" : "craftSlots", "Lnet/minecraft/world/inventory/CraftingContainer;"));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
		insnList.add(new VarInsnNode(Opcodes.ALOAD, null));
        insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/inventory/ResultSlot", obfuscated ? "f_40163_" : "player", "Lnet/minecraft/world/entity/player/Player;"));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(generateHook("firePlayerCraftingEvent", "(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/inventory/ResultSlot;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V"));
		
		debug("#########MC#########");
		if(!!node.owner)debug("MC Node Owner: "+node.owner);
		debug("MC Node Name: "+node.name);
		debug("MC Node Desc: "+node.desc);
		debug("MC Node Var: "+node.var);	
		debug("MC Node Opcodes: "+node.getOpcode());
		debug("#########MC#########");
		var node2 = insnList.get(0);
		debug("#########CT#########");
		if(!!node2.owner)debug("CT Node Owner: "+node2.owner);
		debug("CT Node Name: "+node2.name);
		debug("CT Node Desc: "+node2.desc);
		debug("CT Node Var: "+node2.var);	
		debug("CT Node Opcodes: "+node2.getOpcode());
		debug("#########CT#########");
		
        instructions.insert(node, insnList);
    }
};

function matchesHook(node, owner, name, obfName, desc) {
	
	return !!node.owner && !!node.name && !!node.desc && matchesNode(node, owner, name, obfName, desc);
}

function matchesMethod(node, owner, name, obfName, desc) {

    return node instanceof MethodInsnNode && matchesNode(node, owner, name, obfName, desc);
}

function matchesField(node, owner, name, obfName, desc) {

    return node instanceof FieldInsnNode && matchesNode(node, owner, name, obfName, desc);
}

function matchesNode(node, owner, name, obfName, desc) {

    return node.owner.equals(owner) && (node.name.equals(name) || node.name.equals(obfName)) && node.desc.equals(desc);
}

function generateHook(name, desc) {

    return new MethodInsnNode(Opcodes.INVOKESTATIC, "com/stereowalker/unionlib/hook/UnionHooks", name, desc, false);
}

function getNthNode(node, n) {

    for (var i = 0; i < Math.abs(n); i++) {

        if (n < 0) {

            node = node.getPrevious();
        } else {

            node = node.getNext();
        }
    }

    return node;
}

function log(message) {

    print("[UnionLib Transformer]: " + message);
}

function debug(message) {
	//print("[UnionLib Transformer Debug]: " + message);
}