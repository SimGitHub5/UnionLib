package com.stereowalker.unionlib.client.gui.screens.config.lists;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.client.gui.components.OverflowTextButton;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

@Environment(EnvType.CLIENT)
public class ListList<T extends Object> extends ContainerObjectSelectionList<ListList.Entry> {
	protected ListScreen screen;
	public List<T> mainList;

	public ListList(Minecraft mcIn, ListScreen screen, ConfigValue<List<T>> configList) {
		super(mcIn, screen.width +45, screen.height, 43, screen.height - 32, 25);
		this.screen = screen;
		this.mainList = configList.get();
		if (!configList.get().isEmpty()) {
			int index = 0;
			for (Object val : configList.get()) {
				if (val instanceof String) {
					this.addEntry(new ListList.StringEntry((String) val, index));
				}
				index++;
			}
		}
		this.addEntry(new ListList.AddEntry());
	}

	protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 15 + 40;
	}

	public int getRowWidth() {
		return super.getRowWidth() + 72;
	}

	public void tick() {
		for (Entry ent : this.children()) {
			if (ent instanceof ListList.ListEntry) {
				((ListList<?>.ListEntry<?>)ent).tick();
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public abstract static class Entry extends ContainerObjectSelectionList.Entry<ListList.Entry> {
	}

	@Environment(EnvType.CLIENT)
	public class AddEntry extends ListList.Entry {
		/** The mod */
		protected final Button addButton;

		private AddEntry() {
			this.addButton = new OverflowTextButton(0, 0, 40, 20, new TextComponent("[+]").withStyle(ChatFormatting.GREEN), (onPress) -> {
				ListList.this.remove(ListList.this.mainList.size());
				if (ListList.this.mainList.size() > 0) {
					if (ListList.this.mainList.get(0) instanceof String) {
						ListList.this.mainList.add((T) "");
						ListList.this.addEntry(new ListList.StringEntry("", ListList.this.mainList.size()));
					}
				} else {
					ListList.this.mainList.add((T) "");
					ListList.this.addEntry(new ListList.StringEntry("", ListList.this.mainList.size()));
				}
				ListList.this.addEntry(new ListList.AddEntry());
			});
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			this.addButton.x = p_230432_4_;
			this.addButton.y = p_230432_3_;
			this.addButton.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
		}

		public List<? extends GuiEventListener> children() {
			return ImmutableList.of(this.addButton);
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return ImmutableList.of(this.addButton);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			return this.addButton.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.addButton.mouseReleased(mouseX, mouseY, button);
		}
	}

	@Environment(EnvType.CLIENT)
	public class ListEntry<S> extends ListList.Entry {
		protected S entry;
		protected int index;
		protected final Button removeButton;
		protected AbstractWidget configInteractable;

		public void tick() {
		}
		
		@SuppressWarnings("unchecked")
		protected List<S> getMainList() {
			return (List<S>) ListList.this.mainList;
		}

		private ListEntry(S entry, int index) {
			this.entry = entry;
			this.index = index;
			this.removeButton = new OverflowTextButton(0, 0, 20, 20, new TextComponent("[X]").withStyle(ChatFormatting.RED), (onPress) -> {
				ListList.this.remove(this.index);
				ListList.this.mainList.remove(this.index);
				int i = 0;
				for (Entry ent : ListList.this.children()) {
					if (ent instanceof ListList<?>.StringEntry) {
						((ListList<?>.StringEntry)ent).index = i;
						((ListList<?>.StringEntry)ent).setResponder = false;
						((EditBox)((ListList<?>.StringEntry)ent).configInteractable).setResponder((p_214319_1_) -> {
						});
						
						i++;
					}
					else if (ent instanceof ListList<?>.ListEntry<?>) {
						((ListList<?>.ListEntry<?>)ent).index = i;
						i++;
					}
				}
			});
			this.configInteractable = new OverflowTextButton(0, 0, 20, 20, new TextComponent("NULL").withStyle(ChatFormatting.RED), (onPress) -> {
			});
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			this.removeButton.x = p_230432_4_ + 210;
			this.removeButton.y = p_230432_3_;
			this.removeButton.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
			this.configInteractable.x = p_230432_4_;
			this.configInteractable.y = p_230432_3_;
			this.configInteractable.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
		}

		public List<? extends GuiEventListener> children() {
			return ImmutableList.of(this.removeButton, this.configInteractable);
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return ImmutableList.of(this.removeButton, this.configInteractable);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (this.configInteractable.mouseClicked(mouseX, mouseY, button)) {
				return true;
			} else {
				return this.removeButton.mouseClicked(mouseX, mouseY, button);
			}
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.configInteractable.mouseReleased(mouseX, mouseY, button) || this.removeButton.mouseReleased(mouseX, mouseY, button);
		}

		@Override
		public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
			return this.configInteractable.keyPressed(keyCode, scanCode, modifiers);
		}
		
		@Override
		public boolean charTyped(char pCodePoint, int pModifiers) {
			return this.configInteractable.charTyped(pCodePoint, pModifiers);
		}

		@Override
		public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
			return this.configInteractable.mouseDragged(mouseX, mouseY, button, dragX, dragY);
		}
	}

	@Environment(EnvType.CLIENT)
	public class StringEntry extends ListList<?>.ListEntry<String> {
		boolean setResponder = false;
		private StringEntry(String entry, int index) {
			super(entry, index);
			this.configInteractable = new EditBox(ListList.this.minecraft.font, 0, 0, 200, 20, new TranslatableComponent("config.editBox"));
			((EditBox)this.configInteractable).setValue(entry);
			ListList.this.screen.addChild(this.configInteractable);
		}

		@Override
		public void tick() {
			if (!setResponder) {
				if (this.index < ListList.this.mainList.size()) {
					((EditBox)this.configInteractable).setResponder((p_214319_1_) -> {
						this.getMainList().set(this.index, p_214319_1_);
					});
					this.setResponder = true;
				}
				else {
					//Try to update the index of this element
					int i = 0;
					for (Entry e : ListList.this.children()) {
						if (e == this) {
							this.index = i;
							((EditBox)this.configInteractable).setResponder((p_214319_1_) -> {
								this.getMainList().set(this.index, p_214319_1_);
							});
							this.setResponder = true;
							break;
						}
						i++;
					}
				}
			}
			((EditBox)this.configInteractable).tick();
		}
	}
}
