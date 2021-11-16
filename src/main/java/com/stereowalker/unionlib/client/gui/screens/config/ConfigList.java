package com.stereowalker.unionlib.client.gui.screens.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.client.gui.widget.button.Slider;
import com.stereowalker.unionlib.config.ConfigBuilder;
import com.stereowalker.unionlib.config.ConfigBuilder.Holder;
import com.stereowalker.unionlib.config.UnionConfig;
import com.stereowalker.unionlib.util.RegistryHelper;

import net.minecraft.client.Minecraft;
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
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

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

			Holder holder = ConfigBuilder.getValues(config).get(configValue);
			boolean usesSlider = ConfigBuilder.getValues(config).get(configValue).isUsesSider();
			List<Component> comment = ConfigBuilder.getValues(config).get(configValue).getComments();
			if (ConfigBuilder.getValues(config).get(configValue).getValue().get() instanceof Boolean) {
				this.addEntry(new ConfigList.BooleanEntry(new TranslatableComponent(name2), comment, (ConfigValue<Boolean>) ConfigBuilder.getValues(config).get(configValue).getValue()));
			} else if (ConfigBuilder.getValues(config).get(configValue).getValue().get() instanceof String) {
				this.addEntry(new ConfigList.StringEntry(new TranslatableComponent(name2), comment, (ConfigValue<String>) ConfigBuilder.getValues(config).get(configValue).getValue()));
			} else if (ConfigBuilder.getValues(config).get(configValue).getValue().get() instanceof Enum<?>) {
				this.addEntry(new ConfigList.EnumEntry(new TranslatableComponent(name2), comment, (ConfigValue<Enum<?>>) ConfigBuilder.getValues(config).get(configValue).getValue()));
			} else if (ConfigBuilder.getValues(config).get(configValue).getValue().get() instanceof Number) {
				if (ConfigBuilder.getValues(config).get(configValue).getValue().get() instanceof Double) {
					this.addEntry(new ConfigList.NumberedEntry<Double>(new TranslatableComponent(name2), comment, (ConfigValue<Double>) ConfigBuilder.getValues(config).get(configValue).getValue(), usesSlider, holder.getMin(), holder.getMax()));
				} else if (ConfigBuilder.getValues(config).get(configValue).getValue().get() instanceof Float) {
					this.addEntry(new ConfigList.NumberedEntry<Float>(new TranslatableComponent(name2), comment, (ConfigValue<Float>) ConfigBuilder.getValues(config).get(configValue).getValue(), usesSlider, holder.getMin(), holder.getMax()));
				} else if (ConfigBuilder.getValues(config).get(configValue).getValue().get() instanceof Long) {
					this.addEntry(new ConfigList.NumberedEntry<Long>(new TranslatableComponent(name2), comment, (ConfigValue<Long>) ConfigBuilder.getValues(config).get(configValue).getValue(), usesSlider, holder.getMin(), holder.getMax()));
				} else if (ConfigBuilder.getValues(config).get(configValue).getValue().get() instanceof Short) {
					this.addEntry(new ConfigList.NumberedEntry<Short>(new TranslatableComponent(name2), comment, (ConfigValue<Short>) ConfigBuilder.getValues(config).get(configValue).getValue(), usesSlider, holder.getMin(), holder.getMax()));
				} else if (ConfigBuilder.getValues(config).get(configValue).getValue().get() instanceof Byte) {
					this.addEntry(new ConfigList.NumberedEntry<Byte>(new TranslatableComponent(name2), comment, (ConfigValue<Byte>) ConfigBuilder.getValues(config).get(configValue).getValue(), usesSlider, holder.getMin(), holder.getMax()));
				} else {
					this.addEntry(new ConfigList.NumberedEntry<Integer>(new TranslatableComponent(name2), comment, (ConfigValue<Integer>) ConfigBuilder.getValues(config).get(configValue).getValue(), usesSlider, holder.getMin(), holder.getMax()));
				}
			} else {
				this.addEntry(new ConfigList.ConfigEntry<>(new TranslatableComponent(name2), ConfigBuilder.getValues(config).get(configValue).getComments(), ConfigBuilder.getValues(config).get(configValue).getValue()));
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
		protected final ForgeConfigSpec.ConfigValue<T> config;
		/** The mod */
		protected final Button configButton;

		public void tick() {

		}

		private ConfigEntry(final Component name, final List<Component> comment, final ForgeConfigSpec.ConfigValue<T> config) {
			this.config = config;
			this.configButton = new Button(0, 0, 200, 20, name, (onPress) -> {
			}, (button, stack, x, y) -> {
				int space = ConfigList.this.minecraft.screen.height - y;
				int textHeight = comment.size();

				int drawY = y;

				if (textHeight*15 > space) {
					drawY-= (textHeight*15)-space;
				}
				if (comment != null)
					ConfigList.this.minecraft.screen.renderComponentTooltip(stack, comment, x, drawY);
			});
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			this.configButton.x = p_230432_4_ - 90;
			this.configButton.y = p_230432_3_;
			this.configButton.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
			this.configButton.active = false;
		}

		public List<? extends GuiEventListener> children() {
			return ImmutableList.of(this.configButton);
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return ImmutableList.of(this.configButton);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class EnumEntry extends ConfigList.ConfigEntry<Enum<?>> {
		private final Button enumButton;

		private EnumEntry(final Component name, final List<Component> comment, final ForgeConfigSpec.ConfigValue<Enum<?>> config) {
			super(name, comment, config);
			this.enumButton = new Button(0, 0, 200, 20, name, (onPress) -> {
				config.set(RegistryHelper.rotateEnumForward(config.get(), config.get().getDeclaringClass().getEnumConstants()));
				ConfigBuilder.reload();
			});
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			super.render(p_230432_1_, p_230432_2_, p_230432_3_, p_230432_4_, p_230432_5_, p_230432_6_, p_230432_7_, p_230432_8_, p_230432_9_, p_230432_10_);
			this.enumButton.x = p_230432_4_ + 130;
			this.enumButton.y = p_230432_3_;
			this.enumButton.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
			this.enumButton.setMessage(new TranslatableComponent(config.get().name()));
		}

		public List<? extends GuiEventListener> getEventListeners() {
			return ImmutableList.of(this.enumButton);
		}

		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			return this.enumButton.mouseClicked(mouseX, mouseY, button);
		}

		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.enumButton.mouseReleased(mouseX, mouseY, button);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class BooleanEntry extends ConfigList.ConfigEntry<Boolean> {
		private final Button booleanButton;

		private BooleanEntry(final Component name, final List<Component> comment, final ForgeConfigSpec.ConfigValue<Boolean> config) {
			super(name, comment, config);

			this.booleanButton = new Button(0, 0, 200, 20, name, (onPress) -> {
				config.set(!config.get());
				ConfigBuilder.reload();
			});
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			super.render(p_230432_1_, p_230432_2_, p_230432_3_, p_230432_4_, p_230432_5_, p_230432_6_, p_230432_7_, p_230432_8_, p_230432_9_, p_230432_10_);
			this.booleanButton.x = p_230432_4_ + 130;
			this.booleanButton.y = p_230432_3_;
			this.booleanButton.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
			this.booleanButton.setMessage(CommonComponents.optionStatus(config.get()));
		}

		public List<? extends GuiEventListener> getEventListeners() {
			return ImmutableList.of(this.booleanButton);
		}

		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			return this.booleanButton.mouseClicked(mouseX, mouseY, button);
		}

		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.booleanButton.mouseReleased(mouseX, mouseY, button);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class StringEntry extends ConfigList.ConfigEntry<String> {
		private final EditBox stringField;

		private StringEntry(final Component name, final List<Component> comment, final ForgeConfigSpec.ConfigValue<String> config) {
			super(name, comment, config);

			this.stringField = new EditBox(ConfigList.this.minecraft.font, 0, 0, 200, 20, new TranslatableComponent("config.editBox"));
			this.stringField.setValue(this.config.get());
			this.stringField.setResponder((p_214319_1_) -> {
				this.config.set(p_214319_1_);
				ConfigBuilder.reload();
			});
			ConfigList.this.screen.addChild(this.stringField);
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			super.render(p_230432_1_, p_230432_2_, p_230432_3_, p_230432_4_, p_230432_5_, p_230432_6_, p_230432_7_, p_230432_8_, p_230432_9_, p_230432_10_);
			this.stringField.x = p_230432_4_ + 130;
			this.stringField.y = p_230432_3_;
			this.stringField.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
		}

		@Override
		public void tick() {
			this.stringField.tick();
			super.tick();
		}

		public List<? extends GuiEventListener> getEventListeners() {
			return ImmutableList.of(this.stringField);
		}

		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			return this.stringField.mouseClicked(mouseX, mouseY, button);
		}

		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.stringField.mouseReleased(mouseX, mouseY, button);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class NumberedEntry<V extends Number> extends ConfigList.ConfigEntry<V> {
		private final EditBox stringField;
		private final Slider slider;
		private final boolean useSlider;

		@SuppressWarnings("unchecked")
		private NumberedEntry(final Component name, final List<Component> comment, final ForgeConfigSpec.ConfigValue<V> config, final boolean useSlider, double min, double max) {
			super(name, comment, config);
			this.useSlider = useSlider;

			double shiftedMax = max-min;
			this.slider = new Slider(0, 0, 200, 20, config.get().doubleValue()/shiftedMax, new TextComponent(config.get().toString())) {
				@Override
				protected void applyValue() {
					if (NumberedEntry.this.useSlider) {
						V oldValue = config.get();
						Double newValue = (this.value*shiftedMax)+min;
						try {
							if (config.get() instanceof Double) {
								NumberedEntry.this.config.set((V)newValue);
								ConfigBuilder.reload();
							} else if (config.get() instanceof Float) {
								NumberedEntry.this.config.set((V)(Float)newValue.floatValue());
								ConfigBuilder.reload();
							} else if (config.get() instanceof Long) {
								NumberedEntry.this.config.set((V)(Long)newValue.longValue());
								ConfigBuilder.reload();
							} else if (config.get() instanceof Short) {
								NumberedEntry.this.config.set((V)(Short)newValue.shortValue());
								ConfigBuilder.reload();
							} else if (config.get() instanceof Byte) {
								NumberedEntry.this.config.set((V)(Byte)newValue.byteValue());
								ConfigBuilder.reload();
							} else {
								NumberedEntry.this.config.set((V)(Integer)newValue.intValue());
								ConfigBuilder.reload();
							}
						} catch (NumberFormatException e) {
							config.set(oldValue);
							ConfigBuilder.reload();
						}
						this.setMessage(new TextComponent(NumberedEntry.this.config.get().toString()));
					}
				}
			};
			this.stringField = new EditBox(ConfigList.this.minecraft.font, 0, 0, 200, 20, new TranslatableComponent("config.editNumber")) {
				@Override
				public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
					if (keyCode == GLFW.GLFW_KEY_ENTER) {
						updateValues();
					}
					return super.keyPressed(keyCode, scanCode, modifiers);
				}

				@Override
				protected void onFocusedChanged(boolean focused) {
					super.onFocusedChanged(focused);
				}

				@Override
				public void tick() {
					if (!this.isFocused() && !this.getValue().equals(config.get().toString())) {
						updateValues();
					}
					super.tick();
				}

				public void updateValues() {
					if (!NumberedEntry.this.useSlider) {
						V oldValue = config.get();
						try {
							if (config.get() instanceof Double) {
								NumberedEntry.this.config.set((V)(Double)Double.parseDouble(this.getValue()));
								ConfigBuilder.reload();
							} else if (config.get() instanceof Float) {
								NumberedEntry.this.config.set((V)(Float)Float.parseFloat(this.getValue()));
								ConfigBuilder.reload();
							} else if (config.get() instanceof Long) {
								NumberedEntry.this.config.set((V)(Long)Long.parseLong(this.getValue()));
								ConfigBuilder.reload();
							} else if (config.get() instanceof Short) {
								NumberedEntry.this.config.set((V)(Short)Short.parseShort(this.getValue()));
								ConfigBuilder.reload();
							} else if (config.get() instanceof Byte) {
								NumberedEntry.this.config.set((V)(Byte)Byte.parseByte(this.getValue()));
								ConfigBuilder.reload();
							} else {
								NumberedEntry.this.config.set((V)(Integer)Integer.parseInt(this.getValue()));
								ConfigBuilder.reload();
							}
						} catch (NumberFormatException e) {
							config.set(oldValue);
							ConfigBuilder.reload();
							this.setValue(oldValue.toString());
						}
					}
				}
			};
			this.stringField.setValue(this.config.get().toString());
			ConfigList.this.screen.addChild(this.stringField);
		}

		@Override
		public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
			return this.stringField.keyPressed(keyCode, scanCode, modifiers);
		}

		@Override
		public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
			return this.slider.mouseDragged(mouseX, mouseY, button, dragX, dragY);
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			super.render(p_230432_1_, p_230432_2_, p_230432_3_, p_230432_4_, p_230432_5_, p_230432_6_, p_230432_7_, p_230432_8_, p_230432_9_, p_230432_10_);
			if (this.useSlider) {
				this.slider.x = p_230432_4_ + 130;
				this.slider.y = p_230432_3_;
				this.slider.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
			} else {
				this.stringField.x = p_230432_4_ + 130;
				this.stringField.y = p_230432_3_;
				this.stringField.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
			}
		}

		@Override
		public void tick() {
			this.stringField.tick();
			super.tick();
		}

		public List<? extends GuiEventListener> getEventListeners() {
			return ImmutableList.of(this.stringField, this.slider);
		}

		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (this.slider.mouseClicked(mouseX, mouseY, button)) {
				return true;
			} else {
				return this.stringField.mouseClicked(mouseX, mouseY, button);
			}
		}

		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.stringField.mouseReleased(mouseX, mouseY, button) || this.slider.mouseReleased(mouseX, mouseY, button);
		}
	}
}
