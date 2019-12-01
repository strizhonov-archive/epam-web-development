package by.training.dao;

import by.training.appentry.Bean;
import by.training.connection.ConnectionProvider;
import by.training.dto.GameDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Bean
public class GameDaoImpl implements GameDao {
    private static final String INSERT =
            "INSERT INTO game (bracket_index, start_time, end_time, first_player_id, second_player_id, winner_id, tournament_id) " +
                    "VALUES (?,?,?,?,?,?,?)";
    private static final String INSERT_NEW =
            "INSERT INTO game (bracket_index, tournament_id) " +
                    "VALUES (?,?)";
    private static final String SELECT =
            "SELECT game.game_id, bracket_index, start_time, end_time, first_player_id, second_player_id, winner_id, tournament_id, gameserver.game_server_id " +
                    "FROM game " +
                    "LEFT JOIN gameserver " +
                    "ON game.game_id = gameserver.game_id " +
                    "WHERE game.game_id = ?";
    private static final String UPDATE =
            "UPDATE game " +
                    "SET bracket_index=?, start_time=?, end_time=?, first_player_id=?, second_player_id=?, winner_id=?, tournament_id=? " +
                    "WHERE game_id = ?";
    private static final String DELETE =
            "DELETE FROM game " +
                    "WHERE tournament_id = ?";
    private static final String SELECT_ALL =
            "SELECT game.game_id, bracket_index, start_time, end_time, first_player_id, second_player_id, winner_id, tournament_id , gameserver.game_server_id " +
                    "FROM game " +
                    "LEFT JOIN gameserver " +
                    "ON game.game_id = gameserver.game_id";

    private static final Logger LOGGER = LogManager.getLogger(GameDaoImpl.class);
    private ConnectionProvider provider;
    private DaoMapper mapper;

    public GameDaoImpl(ConnectionProvider provider) {
        this.provider = provider;
        this.mapper = new DaoMapper();
    }

    @Override
    public long save(GameDto gameDto) throws DaoException {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            fillSaveStatement(statement, gameDto);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                return 0;
            }

        } catch (SQLException e) {
            LOGGER.error("Unable to perform entity saving.", e);
            throw new DaoException("Unable to perform entity saving.", e);
        }
    }

    @Override
    public long saveNew(GameDto gameDto) throws DaoException {
        try {
            Connection connection = provider.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(INSERT_NEW, Statement.RETURN_GENERATED_KEYS)) {

                fillNewSaveStatement(statement, gameDto);
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                    return 0;
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Unable to perform entity saving.", e);
            throw new DaoException("Unable to perform entity saving.", e);
        }

    }

    @Override
    public GameDto get(long id) throws DaoException {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                GameDto gameDto = null;
                if (resultSet.next()) {
                    gameDto = mapper.mapGameDto(resultSet);
                }
                if (gameDto == null) {
                    LOGGER.error("Unable to get game with " + id + " id, not found.");
                    throw new DaoException("Unable to get game with " + id + " id, not found.");
                }
                return gameDto;
            }

        } catch (SQLException e) {
            LOGGER.error("Unable to perform entity retrieving.", e);
            throw new DaoException("Unable to perform entity retrieving.", e);
        }
    }

    @Override
    public boolean update(GameDto gameDto) throws DaoException {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            fillUpdateStatement(statement, gameDto);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Unable to perform entity updating.", e);
            throw new DaoException("Unable to perform entity updating.", e);
        }
    }

    @Override
    public boolean delete(long id) throws DaoException {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.error("Unable to perform entity deleting.", e);
            throw new DaoException("Unable to perform entity deleting.", e);
        }
    }

    @Override
    public List<GameDto> findAll() throws DaoException {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            List<GameDto> result = new ArrayList<>();
            while (resultSet.next()) {
                GameDto gameDto = mapper.mapGameDto(resultSet);
                result.add(gameDto);
            }
            return result;

        } catch (SQLException e) {
            LOGGER.error("Unable to perform all entities retrieving.", e);
            throw new DaoException("Unable to perform all entities retrieving.", e);
        }
    }


    @Override
    public List<GameDto> findLatest(int maxGameResults) throws DaoException {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            List<GameDto> result = new ArrayList<>();
            while (resultSet.next()) {
                GameDto gameDto = mapper.mapGameDto(resultSet);
                result.add(gameDto);
            }

            return result;

        } catch (SQLException e) {
            LOGGER.error("Unable to perform entities retrieving.", e);
            throw new DaoException("Unable to perform entities retrieving.", e);
        }
    }

    private void fillSaveStatement(PreparedStatement statement, GameDto game) throws SQLException {
        int i = 0;
        statement.setInt(++i, game.getBracketIndex());
        java.sql.Date sqlStartTime = DaoUtil.toSqlDate(game.getStartTime());
        statement.setDate(++i, sqlStartTime);
        java.sql.Date sqlEndTime = DaoUtil.toSqlDate(game.getEndTime());
        statement.setDate(++i, sqlEndTime);
        statement.setLong(++i, game.getFirstPlayerId());
        statement.setLong(++i, game.getSecondPlayerId());
        statement.setLong(++i, game.getWinnerId());
        statement.setLong(++i, game.getTournamentId());
    }

    private void fillNewSaveStatement(PreparedStatement statement, GameDto game) throws SQLException {
        int i = 0;
        statement.setInt(++i, game.getBracketIndex());
        statement.setLong(++i, game.getTournamentId());
    }

    private void fillUpdateStatement(PreparedStatement statement, GameDto game) throws SQLException {
        int i = 0;
        statement.setInt(++i, game.getBracketIndex());
        java.sql.Date sqlStartTime = DaoUtil.toSqlDate(game.getStartTime());
        statement.setDate(++i, sqlStartTime);
        java.sql.Date sqlEndTime = DaoUtil.toSqlDate(game.getEndTime());
        statement.setDate(++i, sqlEndTime);
        statement.setLong(++i, game.getFirstPlayerId());
        statement.setLong(++i, game.getSecondPlayerId());
        statement.setLong(++i, game.getWinnerId());
        statement.setLong(++i, game.getTournamentId());
        statement.setLong(++i, game.getId());
    }

}