package com.xlogant.controlador;

import com.xlogant.conecta.ConectaDB;
import com.xlogant.modelo.menu.MenuItemDTO;
import com.xlogant.modelo.menu.SubMenuItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para cargar la estructura del menú de un usuario desde la base de datos.
 */
public class MenuService {

    private static final String USER_MENU_QUERY =
            "SELECT " +
            "    mp.nombre_menu, " +
            "    sp.nombre_submenu, " +
            "    csm.estatus_submenus " +
            "FROM control_menu cm " +
            "JOIN menu_principal mp ON cm.id_menu = mp.id_menu " +
            "JOIN control_submenu csm ON cm.id_menu = csm.id_menu AND cm.id_usuario = csm.id_usuario " +
            "JOIN submenu_principal sp ON csm.id_submenu = sp.id_submenu " +
            "WHERE cm.id_usuario = ? " +
            "  AND cm.id_role = ? " +
            "  AND cm.estatus_menu = TRUE " +
            "ORDER BY mp.nombre_menu, sp.nombre_submenu";

    public List<MenuItemDTO> getMenuItemsForUser(long userId, long roleId) throws SQLException {
        Map<String, List<SubMenuItemDTO>> menuData = new LinkedHashMap<>();

        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(USER_MENU_QUERY)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, roleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String menuName = rs.getString("nombre_menu");
                    menuData.computeIfAbsent(menuName, k -> new ArrayList<>())
                            .add(new SubMenuItemDTO(rs.getString("nombre_submenu"), rs.getBoolean("estatus_submenus")));
                }
            }
        }

        return menuData.entrySet().stream()
                .map(entry -> new MenuItemDTO(entry.getKey(), entry.getValue()))
                .toList();
    }
}