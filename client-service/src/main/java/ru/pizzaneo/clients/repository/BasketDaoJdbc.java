package ru.pizzaneo.clients.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.pizzaneo.clients.domain.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class BasketDaoJdbc implements BasketDao {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final Logger logger = LoggerFactory.getLogger(BasketDaoJdbc.class);

    public BasketDaoJdbc(NamedParameterJdbcOperations jdbcTemplate) {
        this.jdbcOperations = jdbcTemplate;
    }

    @Override
    public List<String> findByChatId(long chatId) {
        try {
            return jdbcOperations.queryForList("SELECT PRODUCT_UUID FROM BASKET WHERE CHAT_ID = :CHAT_ID"
                    , new MapSqlParameterSource() {{
                        addValue("CHAT_ID", chatId);
                    }}
                    , String.class);
        } catch (DataAccessException e) {
            logger.warn("not found client basket");
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> findHistoryByChatId(long chatId) {
        try {
            return jdbcOperations.queryForList("SELECT PRODUCT_UUID FROM BASKET_HISTORY WHERE CHAT_ID = :CHAT_ID"
                    , new MapSqlParameterSource() {{
                        addValue("CHAT_ID", chatId);
                    }}
                    , String.class);
        } catch (DataAccessException e) {
            logger.warn("not found client history basket");
        }
        return new ArrayList<>();
    }

    @Override
    public void saveClientBasket(Client client) {
        HashMap<String, Object> params = new HashMap<>() {{
            put("CHAT_ID", client.getChatId());
        }};
        jdbcOperations.update("DELETE FROM BASKET WHERE CHAT_ID = :CHAT_ID", params);
        for (String product : client.getBasketVariationIds()) {
            params.put("PRODUCT_UUID", product);
            jdbcOperations.update("INSERT INTO BASKET (CHAT_ID, PRODUCT_UUID)" +
                    " VALUES (:CHAT_ID, :PRODUCT_UUID)", params);
        }
    }

    @Override
    public void updateClientHistory(final Client client) {
        List<String> curBasket = this.findByChatId(client.getChatId());
        List<String> stmtParts = new ArrayList<>();
        if (curBasket.size() != 0) {
            for (String productUuid : curBasket) {
                stmtParts.add(String.format("(%d, '%s')", client.getChatId(), productUuid));
            }
            MapSqlParameterSource src = new MapSqlParameterSource();
            src.addValue("CHAT_ID", client.getChatId());
            this.jdbcOperations.update("DELETE FROM BASKET_HISTORY WHERE CHAT_ID = :CHAT_ID", src);
            this.jdbcOperations.update("INSERT INTO BASKET_HISTORY (CHAT_ID, PRODUCT_UUID) VALUES" +
                    String.join(", ", stmtParts), new HashMap<>());
        }
    }
}
