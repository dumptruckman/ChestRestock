package com.dumptruckman.chestrestock;

import com.dumptruckman.actionmenu2.api.AbstractSignView;
import com.dumptruckman.actionmenu2.api.Menu;
import com.dumptruckman.actionmenu2.api.MenuModel;
import com.dumptruckman.actionmenu2.api.event.MenuEvent;
import com.dumptruckman.actionmenu2.api.event.MenuListener;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class SignView extends AbstractSignView implements MenuListener {
    
    public static final int SIGN_LINES = 4;
    
    int leadIndex = 0;

    public SignView(Sign sign) {
        super(sign);
    }

    @Override
    public void showMenu(Menu menu, Player player) {
        if (menu == null) {
            return;
        }
        Sign sign = getSign();
        MenuModel menuModel = menu.getModel();
        for (int i = this.leadIndex; i < this.leadIndex + SIGN_LINES; i++) {
            String text = "";
            if (i < menuModel.size()) {
                text = menuModel.get(i).getText();
                if (menuModel.get(i).isSelectable()) {
                    if (menuModel.getSelectedIndex() == i) {
                        text = ChatColor.RED + text;
                    }
                }
            }
            StringBuilder builder = new StringBuilder();
            builder.append(text);
            for (int j = text.length(); j <= 16; j++) {
                builder.append(" ");
            }
            text = builder.toString();
            sign.setLine(i, text);
        }
        sign.update(true);
    }

    @Override
    public void onSelectionChange(MenuEvent event) {
        if (event.getIndex1() > event.getIndex0()) {
            int menuSize = event.getModel().size();
            if (event.getIndex1() >= menuSize - 1) {
                this.leadIndex = menuSize - SIGN_LINES;
            } else {
                this.leadIndex = event.getIndex1() - 2;
                if (this.leadIndex < 0) {
                    this.leadIndex = 0;
                }
            }
        } else if (event.getIndex1() < event.getIndex0()) {
            if (event.getIndex1() <= 0) {
                this.leadIndex = 0;
            } else {
                this.leadIndex = event.getIndex1() - 1;
            }
        }
    }

    @Override
    public void onContentsAdd(MenuEvent menuEvent) { }

    @Override
    public void onContentsRemove(MenuEvent menuEvent) { }

    @Override
    public void onContentsChange(MenuEvent menuEvent) { }
}
