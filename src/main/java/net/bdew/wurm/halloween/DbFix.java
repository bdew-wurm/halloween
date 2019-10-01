package net.bdew.wurm.halloween;

import com.wurmonline.server.DbConnector;
import com.wurmonline.server.items.Materials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbFix {
    public static void fixMaskMaterial() throws SQLException {
        Connection conn = DbConnector.getItemDbCon();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE ITEMS SET MATERIAL=? WHERE TEMPLATEID=? AND MATERIAL=?")) {
            ps.setByte(1, Materials.MATERIAL_WOOD_OLEANDER);
            ps.setInt(2, CustomItems.maskId);
            ps.setByte(3, Materials.MATERIAL_SLATE);
            long started = System.currentTimeMillis();
            int updated = ps.executeUpdate();
            Halloween.logInfo(String.format("Updated material for %d masks in the database, this took %dms", updated, System.currentTimeMillis() - started));
        } finally {
            DbConnector.returnConnection(conn);
        }
    }
}
