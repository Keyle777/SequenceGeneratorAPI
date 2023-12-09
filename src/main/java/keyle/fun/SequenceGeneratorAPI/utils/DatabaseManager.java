package keyle.fun.SequenceGeneratorAPI.utils;

import keyle.fun.SequenceGeneratorAPI.entity.SequenceNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;

/**
 * 数据库管理工具类，用于创建表和执行 SQL 操作。
 */
@Component
public class DatabaseManager {

    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    @Resource
    private DataSource dataSource;

    // 创建表的 SQL 语句
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS `sequence_number` ("
            + "`id` bigint(20) NOT NULL AUTO_INCREMENT, "
            + "`sequence_no` varchar(255) NOT NULL, "
            + "`create_time` datetime DEFAULT NULL, "
            + "`update_time` datetime DEFAULT NULL, "
            + "PRIMARY KEY (`id`)"
            + ") ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1";

    // 插入数据的 SQL 语句
    private static final String INSERT_DATA_SQL = "INSERT INTO `sequence_number` "
            + "(`sequence_no`, `create_time`, `update_time`) "
            + "VALUES (?, ?, ?)";

    // 根据序列号查询的 SQL 语句
    private static final String FIND_BY_SEQUENCE_NO_SQL = "SELECT id FROM sequence_number WHERE sequence_no = ?";

    /**
     * 创建表，如果表不存在的话。
     */
    public void createTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(CREATE_TABLE_SQL);
            log.info("表已成功创建。");

        } catch (SQLException e) {
            log.error("创建表时发生错误", e);
        }
    }

    /**
     * 插入数据到数据库。
     *
     * @param sequenceNumber 要插入的 SequenceNumber 对象
     */
    public void insert(SequenceNumber sequenceNumber) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            // 关闭自动提交，开启事务
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(INSERT_DATA_SQL)) {
                statement.setString(1, sequenceNumber.getSequenceNo());
                statement.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
                statement.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));

                statement.executeUpdate();
                log.info("数据插入成功。");

                // 提交事务
                connection.commit();
            } catch (SQLException e) {
                // 发生异常时回滚事务
                connection.rollback();
                log.error("插入数据时发生错误", e);
            }
        } catch (SQLException e) {
            log.error("获取数据库连接时发生错误", e);
        } finally {
            try {
                if (connection != null) {
                    // 在事务后恢复自动提交为true
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("关闭数据库连接时发生错误", e);
            }
        }
    }


    /**
     * 根据序列号查询数据库。
     *
     * @param sequenceNo 要查询的序列号
     * @return 查询到的 SequenceNumber 对象，如果未找到返回 null
     */
    public SequenceNumber findBySequenceNo(String sequenceNo) {
        SequenceNumber result = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_SEQUENCE_NO_SQL)) {

            statement.setString(1, sequenceNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new SequenceNumber();
                    result.setId(resultSet.getLong("id"));
                }
            }

        } catch (SQLException e) {
            log.error("根据序列号查询时发生错误", e);
        }

        return result;
    }
}
