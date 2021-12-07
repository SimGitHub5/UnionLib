package com.stereowalker.unionlib.client.gui.screens.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.client.gui.components.OverflowTextButton;
import com.stereowalker.unionlib.client.gui.screens.config.lists.ListScreen;
import com.stereowalker.unionlib.client.gui.widget.button.Slider;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.config.ConfigBuilder.Holder;
import com.stereowalker.unionlib.config.UnionConfig;
import com.stereowalker.unionlib.util.RegistryHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfigList extends ContainerObjectSelectionList<ConfigList.Entry> {
	protected ConfigScreen screen;

	@SuppressWarnings("unchecked")
	public ConfigList(Minecraft mcIn, ConfigScreen screen, UnionConfig config) {
		super(mcIn, screen.width +45, screen.height, 43, screen.height - 32, 25);
		this.screen = screen;
		List<String> c = Arrays.asList(ConfigBuilder.getValues(config).keySet().toArray(new String[0]));
		Collections.sort(c);

		String currentCategory = "";
		for (String configValue : c) {
			String name = "";
			for (int i = 1; i < configValue.split("=").length; i++) {
				name += configValue.split("=")[i];
			}
			if (split(name).size() > 1 && !split(name).get(0).equals(currentCategory)) {
				this.addEntry(new ConfigList.CategoryEntry(new TranslatableComponent(split(name).get(0))));
				currentCategory = split(name).get(0);
			}

			String name2 = "";
			if (split(name).size() > 1) {
				for (int i = 1; i < split(name).size(); i++) {
					name2 += split(name).get(i);
				}
			} else {
				name2 = name;
			}

			Holder<?> holder = ConfigBuilder.getValues(config).get(configValue);
			if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof Boolean) {
				this.addEntry(new ConfigList.BooleanEntry(new TranslatableComponent(name2), (Holder<Boolean>) holder));
			} else if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof String) {
				this.addEntry(new ConfigList.StringEntry(new TranslatableComponent(name2), (Holder<String>) holder));
			} else if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof Enum<?>) {
				this.addEntry(new ConfigList.EnumEntry(new TranslatableComponent(name2), (Holder<Enum<?>>) holder));
			} else if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof Number) {
				if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof Double) {
					this.addEntry(new ConfigList.NumberedEntry<Double>(new TranslatableComponent(name2), (Holder<Double>) holder));
				} else if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof Float) {
					this.addEntry(new ConfigList.NumberedEntry<Float>(new TranslatableComponent(name2), (Holder<Float>) holder));
				} else if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof Long) {
					this.addEntry(new ConfigList.NumberedEntry<Long>(new TranslatableComponent(name2), (Holder<Long>) holder));
				} else if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof Short) {
					this.addEntry(new ConfigList.NumberedEntry<Short>(new TranslatableComponent(name2), (Holder<Short>) holder));
				} else if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof Byte) {
					this.addEntry(new ConfigList.NumberedEntry<Byte>(new TranslatableComponent(name2), (Holder<Byte>) holder));
				} else {
					this.addEntry(new ConfigList.NumberedEntry<Integer>(new TranslatableComponent(name2), (Holder<Integer>) holder));
				}
			} else if (ConfigBuilder.getValues(config).get(configValue).getDefaultValue() instanceof List<?>) {
				this.addEntry(new ConfigList.ListEntry(new TranslatableComponent(name2), (Holder<List<?>>) holder));
			} else {
				this.addEntry(new ConfigList.ConfigEntry<>(new TranslatableComponent(name2), holder));
			}
		}
	}

	private static final Splitter DOT_SPLITTER = Splitter.on(".");
	private static List<String> split(String path)
	{
		return Lists.newArrayList(DOT_SPLITTER.split(path));
	}

	protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 15 + 40;
	}

	public int getRowWidth() {
		return super.getRowWidth() + 72;
	}

	public void tick() {
		for (Entry ent : this.children()) {
			if (ent instanceof ConfigList.ConfigEntry) {
				((ConfigList.ConfigEntry<?>)ent).tick();
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public abstract static class Entry extends ContainerObjectSelectionList.Entry<ConfigList.Entry> {
	}

	@OnlyIn(Dist.CLIENT)
	public class CategoryEntry extends ConfigList.Entry {
		private final Component labelText;
		private final int labelWidth;

		public CategoryEntry(Component p_i232280_2_) {
			this.labelText = p_i232280_2_;
			this.labelWidth = ConfigList.this.minecraft.font.width(this.labelText);
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			ConfigList.this.minecraft.font.draw(p_230432_1_, this.labelText, (float)(ConfigList.this.minecraft.screen.width / 2 - this.labelWidth / 2), (float)(p_230432_3_ + p_230432_6_ - 9 - 1), 16777215);
		}

		public boolean changeFocus(boolean focus) {
			return false;
		}

		public List<? extends GuiEventListener> children() {
			return Collections.emptyList();
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return Collections.emptyList();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class ConfigEntry<T> extends ConfigList.Entry {
		protected final Holder<T> config;
		/** The mod */
		protected final Button configButton;
		protected AbstractWidget configInteractable;
		protected final Button resetButton;
		protected final int interactionWidth;

		public void tick() {

		}

		public void reset(Component name) {
			this.configInteractable = null;
		}

		private ConfigEntry(final Component name, final Holder<T> config) {
			this.config = config;
			this.interactionWidth = 140;
			this.configButton = new OverflowTextButton(0, 0, 195, 20, name, (onPress) -> {
			}, (button, stack, x, y) -> {
				int space = ConfigList.this.minecraft.screen.height - y;
				int textHeight = config.getComments().size();

				int drawY = y;

				if (textHeight*15 > space) {
					drawY-= (textHeight*15)-space;
				}
				if (config.getComments() != null)
					ConfigList.this.minecraft.screen.renderComponentTooltip(stack, config.getComments(), x, drawY);
			});
			this.reset(name);
			this.resetButton = new OverflowTextButton(0, 0, 40, 20, new TranslatableComponent("controls.reset"), (onPress) -> {
				this.config.getValue().set(this.config.getDefaultValue());
				this.reset(name);
			});
			ConfigList.this.screen.addChild(this.configInteractable);
			ConfigList.this.screen.addChild(this.resetButton);
		}

		@Override
		public void render(PoseStack pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, 
				int pMouseY, boolean pIsMouseOver, float pPartialTick) {
			this.configButton.x = pLeft - 80;
			this.configButton.y = pTop;
			this.configButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
			this.configButton.active = false;
			if (configInteractable != null) {
				this.configInteractable.x = pLeft + 125;
				this.configInteractable.y = pTop;
				this.configInteractable.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
			}
			this.resetButton.x = pLeft + 275;
			this.resetButton.y = pTop;
			this.resetButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
			this.resetButton.active = !this.config.getValue().get().equals(this.config.getDefaultValue());
		}

		public List<? extends GuiEventListener> children() {
			return this.configInteractable == null ? ImmutableList.of(this.configButton, this.resetButton) : ImmutableList.of(this.configButton, this.resetButton, this.configInteractable);
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return this.configInteractable == null ? ImmutableList.of(this.configButton, this.resetButton) : ImmutableList.of(this.configButton, this.resetButton, this.configInteractable);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (this.resetButton.mouseClicked(mouseX, mouseY, button)) {
				return true;
			} else if (this.configInteractable != null && this.configInteractable.mouseClicked(mouseX, mouseY, button)) {
				return true;
			} else {
				return this.configButton.mouseClicked(mouseX, mouseY, button);
			}
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.resetButton.mouseReleased(mouseX, mouseY, button) || (this.configInteractable != null && this.configInteractable.mouseReleased(mouseX, mouseY, button)) || this.configButton.mouseReleased(mouseX, mouseY, button);

		}

		@Override
		public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
			if (this.configInteractable != null) return this.configInteractable.keyPressed(keyCode, scanCode, modifiers);
			else return super.keyPressed(keyCode, scanCode, modifiers);
		}

		@Override
		public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
			if (this.configInteractable != null && this.configInteractable.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
				return true;
			} else if (this.resetButton.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
				return true;
			} else {
				return this.configButton.mouseDragged(mouseX, mouseY, button, dragX, dragY);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class EnumEntry extends ConfigList.ConfigEntry<Enum<?>> {
		private EnumEntry(final Component name, final Holder<Enum<?>> config) {
			super(name, config);
		}

		@Override
		public void reset(Component name) {
			this.configInteractable = new Button(0, 0, this.interactionWidth, 20, name, (onPress) -> {
				config.getValue().set(RegistryHelper.rotateEnumForward(config.getValue().get(), config.getValue().get().getDeclaringClass().getEnumConstants()));
				ConfigBuilder.reload();
			});
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			super.render(p_230432_1_, p_230432_2_, p_230432_3_, p_230432_4_, p_230432_5_, p_230432_6_, p_230432_7_, p_230432_8_, p_230432_9_, p_230432_10_);
			this.configInteractable.setMessage(new TranslatableComponent(config.getValue().get().name()));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class BooleanEntry extends ConfigList.ConfigEntry<Boolean> {
		private BooleanEntry(final Component name, final Holder<Boolean> config) {
			super(name, config);
		}

		@Override
		public void reset(Component name) {
			this.configInteractable = new Button(0, 0, this.interactionWidth, 20, name, (onPress) -> {
				config.getValue().set(!config.getValue().get());
				ConfigBuilder.reload();
			});
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			super.render(p_230432_1_, p_230432_2_, p_230432_3_, p_230432_4_, p_230432_5_, p_230432_6_, p_230432_7_, p_230432_8_, p_230432_9_, p_230432_10_);
			this.configInteractable.setMessage(CommonComponents.optionStatus(config.getValue().get()));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class StringEntry extends ConfigList.ConfigEntry<String> {
		private StringEntry(final Component name, final Holder<String> config) {
			super(name, config);
		}

		@Override
		public void reset(Component name) {
			if (this.configInteractable instanceof EditBox) {
				((EditBox) this.configInteractable).setValue(this.config.getValue().get());
			} else {
				this.configInteractable = new EditBox(ConfigList.this.minecraft.font, 0, 0, this.interactionWidth, 20, new TranslatableComponent("config.editBox"));
				((EditBox) this.configInteractable).setValue(this.config.getValue().get());
				((EditBox)this.configInteractable).setResponder((p_214319_1_) -> {
					System.out.println("Resp: "+p_214319_1_);
					this.config.getValue().set(p_214319_1_);
					ConfigBuilder.reload();
				});
			}
		}

		@Override
		public void tick() {
			//			System.out.println(((EditBox)this.configInteractable).getValue());
			((EditBox)this.configInteractable).tick();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class NumberedEntry<V extends Number> extends ConfigList.ConfigEntry<V> {
		private NumberedEntry(final Component name, final Holder<V> config) {
			super(name, config);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void reset(Component name) {
			double shiftedMax = this.config.getMax()-this.config.getMin();
			if (this.config.isUsingSlider()) {
				if (this.configInteractable instanceof Slider) {
					((Slider)this.configInteractable).setValue(config.getValue().get().doubleValue()/shiftedMax);
				} else {
					this.configInteractable = new Slider(0, 0, this.interactionWidth, 20, config.getValue().get().doubleValue()/shiftedMax, new TextComponent(config.getValue().get().toString()));
					((Slider)this.configInteractable).setResponder((val) -> {
						if (NumberedEntry.this.config.isUsingSlider()) {
							V oldValue = config.getValue().get();
							Double newValue = (val*shiftedMax)+NumberedEntry.this.config.getMin();
							try {
								if (config.getValue().get() instanceof Double) this.config.getValue().set((V)newValue);
								 else if (config.getValue().get() instanceof Float) this.config.getValue().set((V)(Float)newValue.floatValue());
								 else if (config.getValue().get() instanceof Long) this.config.getValue().set((V)(Long)newValue.longValue());
								 else if (config.getValue().get() instanceof Short) this.config.getValue().set((V)(Short)newValue.shortValue());
								 else if (config.getValue().get() instanceof Byte) this.config.getValue().set((V)(Byte)newValue.byteValue());
								 else this.config.getValue().set((V)(Integer)newValue.intValue());
							} catch (NumberFormatException e) {
								config.getValue().set(oldValue);
							}
							ConfigBuilder.reload();
							((Slider)this.configInteractable).setMessage(new TextComponent(this.config.getValue().get().toString()));
						}
					});
				}
			} else {
				if (this.configInteractable instanceof EditBox) {
					((EditBox)this.configInteractable).setValue(this.config.getValue().get().toString());
				} else {
					this.configInteractable = new EditBox(ConfigList.this.minecraft.font, 0, 0, this.interactionWidth, 20, new TranslatableComponent("config.editNumberBox"));
					((EditBox)this.configInteractable).setValue(this.config.getValue().get().toString());
					((EditBox)this.configInteractable).setFilter((text) -> {
						V oldValue = config.getValue().get();
						V newValue = null;
						try {
							if (oldValue instanceof Double) newValue = (V)(Double)Double.parseDouble(text);
							else if (oldValue instanceof Float) newValue = (V)(Float)Float.parseFloat(text);
							else if (oldValue instanceof Long) newValue = (V)(Long)Long.parseLong(text);
							else if (oldValue instanceof Short) newValue = (V)(Short)Short.parseShort(text);
							else if (oldValue instanceof Byte) newValue = (V)(Byte)Byte.parseByte(text);
							else newValue = (V)(Integer)Integer.parseInt(text);
							return newValue != null;
						} catch (NumberFormatException e) {
							return false;
						}
					});
					((EditBox)this.configInteractable).setResponder((text) -> {
						V oldValue = config.getValue().get();
						V newValue = null;
						try {
							if (oldValue instanceof Double) newValue = (V)(Double)Double.parseDouble(text);
							else if (oldValue instanceof Float) newValue = (V)(Float)Float.parseFloat(text);
							else if (oldValue instanceof Long) newValue = (V)(Long)Long.parseLong(text);
							else if (oldValue instanceof Short) newValue = (V)(Short)Short.parseShort(text);
							else if (oldValue instanceof Byte) newValue = (V)(Byte)Byte.parseByte(text);
							else newValue = (V)(Integer)Integer.parseInt(text);

							config.getValue().set(newValue);
							ConfigBuilder.reload();
						} catch (NumberFormatException e) {
						}
					});
				}
			}
		}

		@Override
		public void tick() {
			if (this.configInteractable instanceof EditBox) ((EditBox)this.configInteractable).tick();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class ListEntry extends ConfigList.ConfigEntry<List<?>> {
		private ListEntry(final Component name, final Holder<List<?>> config) {
			super(name, config);
			this.configInteractable = new Button(0, 0, this.interactionWidth, 20, new TranslatableComponent("list"), (onPress) -> {
				ConfigList.this.minecraft.setScreen(new ListScreen(name, ConfigList.this.screen, config.getValue()));
			});
		}
	}
}
