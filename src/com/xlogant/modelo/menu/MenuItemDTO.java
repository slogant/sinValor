package com.xlogant.modelo.menu;

import java.util.List;

/**
 * Representa un menú principal con su lista de sub-ítems.
 */
public record MenuItemDTO(String name, List<SubMenuItemDTO> subItems) {
}