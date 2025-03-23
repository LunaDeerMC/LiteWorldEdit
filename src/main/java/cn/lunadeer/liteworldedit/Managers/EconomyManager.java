package cn.lunadeer.liteworldedit.Managers;

import cn.lunadeer.liteworldedit.LiteWorldEdit;
import cn.lunadeer.liteworldedit.LoggerX;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {
    public EconomyManager() {
        recheck();
    }

    public void recheck() {
        if (LiteWorldEdit.instance.getConfigMgr().isEconomyEnabled()) {
            RegisteredServiceProvider<Economy> rsp = _plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                econ = rsp.getProvider();
                _is_economy_supported = true;
                return;
            }
            LoggerX.warn("Vault not available.");
        } else {
            LoggerX.info("Vault has been disabled.");
        }
        _is_economy_supported = false;
    }

    public String currencyNamePlural() {
        return econ.currencyNamePlural();
    }

    public String currencyNameSingular() {
        return econ.currencyNameSingular();
    }

    public void withdrawPlayer(OfflinePlayer player, double amount) {
        econ.withdrawPlayer(player, amount);
    }

    public void depositPlayer(OfflinePlayer player, double amount) {
        econ.depositPlayer(player, amount);
    }

    public double getBalance(OfflinePlayer player) {
        return econ.getBalance(player);
    }

    public boolean isEconomyExist() {
        return _is_economy_supported;
    }

    private final LiteWorldEdit _plugin = LiteWorldEdit.instance;

    private Boolean _is_economy_supported;

    private Economy econ = null;
}
