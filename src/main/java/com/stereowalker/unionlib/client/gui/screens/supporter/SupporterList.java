package com.stereowalker.unionlib.client.gui.screens.supporter;

import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stereowalker.unionlib.supporter.Supporters;
import com.stereowalker.unionlib.supporter.Supporters.Supporter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SupporterList extends ContainerObjectSelectionList<SupporterList.Entry> {
	protected SupporterScreen screen;

	public SupporterList(Minecraft mcIn, SupporterScreen screen) {
		super(mcIn, screen.width +45, screen.height, 43, screen.height - 32, 25);
		this.screen = screen;
		List<Supporter> c = Supporters.SUPPORTERS;

		String currentCategory = "";
		System.out.println();
		Style linkStyle = Style.EMPTY.withBold(true).withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.patreon.com/Stereowalker"));
		for (Supporter supporter : c) {
			if (supporter.type().disp) {
				Style catStyle = Style.EMPTY.withBold(true).withUnderlined(true).withColor(supporter.type().sty);
				if (!supporter.type().name().toLowerCase().equals(currentCategory)) {
					currentCategory = supporter.type().name().toLowerCase();
					this.addEntry(new SupporterList.CategoryEntry(new TextComponent(currentCategory.substring(0, 1).toUpperCase()+currentCategory.substring(1)).withStyle(catStyle)));
				}
				this.addEntry(new SupporterList.CategoryEntry(new TextComponent(supporter.displayName()).withStyle(Style.EMPTY.withColor(supporter.type().sty))));
			}
		}
		this.addEntry(new SupporterList.CategoryEntry(new TextComponent("If you want you want to be a part of these wonderful people, you can join them")));
		this.addEntry(new SupporterList.CategoryEntry(new TextComponent("here").withStyle(linkStyle)));
		
	}

	protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 15 + 40;
	}

	public int getRowWidth() {
		return super.getRowWidth() + 72;
	}

	@OnlyIn(Dist.CLIENT)
	public abstract static class Entry extends ContainerObjectSelectionList.Entry<SupporterList.Entry> {
	}

	@OnlyIn(Dist.CLIENT)
	public class CategoryEntry extends SupporterList.Entry {
		private final Component labelText;
		private final int labelWidth;

		public CategoryEntry(Component p_i232280_2_) {
			this.labelText = p_i232280_2_;
			this.labelWidth = SupporterList.this.minecraft.font.width(this.labelText);
		}

		public void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			SupporterList.this.minecraft.font.draw(p_230432_1_, this.labelText, (float)(SupporterList.this.minecraft.screen.width / 2 - this.labelWidth / 2), (float)(p_230432_3_ + p_230432_6_ - 9 - 1), 16777215);
		}

		public boolean changeFocus(boolean focus) {
			return false;
		}

		public List<? extends GuiEventListener> children() {
			return Collections.emptyList();
		}
		
		@Override
		public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
			if (this.labelText.getStyle() != null && this.labelText.getStyle().getClickEvent() != null) {
				SupporterList.this.screen.handleComponentClicked(this.labelText.getStyle());
			}
			return super.mouseClicked(pMouseX, pMouseY, pButton);
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return Collections.emptyList();
		}
	}
}
