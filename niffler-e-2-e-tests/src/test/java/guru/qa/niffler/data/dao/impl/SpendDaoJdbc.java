package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private final Connection connection;

    public SpendDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SpendEntity create(SpendEntity spend) {

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, spend.getUsername());
            ps.setDate(2, spend.getSpendDate());
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            spend.setId(generatedKey);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {

        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * from spend WHERE id = ?"
        )) {
            ps.setObject(1, id);

            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(createSpendEntity(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * from spend WHERE username = ?"
        )) {
            ps.setObject(1, username);

            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                List<SpendEntity> foundSpends = new ArrayList<>();
                while (rs.next()) {
                    foundSpends.add(createSpendEntity(rs));
                }
                return foundSpends;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(SpendEntity spend) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE from spend WHERE id = ?"
        )) {
            ps.setObject(1, spend.getId());

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SpendEntity createSpendEntity(ResultSet rs) throws SQLException {
        SpendEntity se = new SpendEntity();
        se.setId(rs.getObject("id", UUID.class));
        se.setUsername(rs.getString("username"));
        se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        se.setSpendDate(rs.getDate("spend_date"));
        se.setAmount(rs.getDouble("amount"));
        se.setDescription(rs.getString("description"));
        CategoryEntity category = new CategoryEntity();
        category.setId(UUID.fromString(rs.getString("category_id")));
        se.setCategory(category);
        return se;
    }
}
