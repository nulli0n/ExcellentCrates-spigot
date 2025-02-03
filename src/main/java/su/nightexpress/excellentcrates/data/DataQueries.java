package su.nightexpress.excellentcrates.data;

import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.data.crate.GlobalCrateData;
import su.nightexpress.excellentcrates.data.reward.RewardLimit;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.nightcore.db.sql.query.impl.DeleteQuery;
import su.nightexpress.nightcore.db.sql.query.impl.InsertQuery;
import su.nightexpress.nightcore.db.sql.query.impl.UpdateQuery;
import su.nightexpress.nightcore.db.sql.util.WhereOperator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Function;

public class DataQueries {

    public static final Function<ResultSet, GlobalCrateData> CRATE_DATA_LOADER = resultSet -> {
        try {
            String crateId = resultSet.getString(DataHandler.COLUMN_CRATE_ID.getName());

            UUID latestOpenerId = null;

            String uuidStr = resultSet.getString(DataHandler.COLUMN_LATEST_OPENER_ID.getName());
            if (!uuidStr.equalsIgnoreCase("null")) {
                latestOpenerId = UUID.fromString(uuidStr);
            }

            String openerName = resultSet.getString(DataHandler.COLUMN_LATEST_OPENER_NAME.getName());
            if (openerName.equalsIgnoreCase("null")) openerName = null;

            String rewardId = resultSet.getString(DataHandler.COLUMN_LATEST_REWARD_ID.getName());
            if (rewardId.equalsIgnoreCase("null")) rewardId = null;

            return new GlobalCrateData(crateId, latestOpenerId, openerName, rewardId);
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    };

    public static final Function<ResultSet, RewardLimit> REWARD_LIMIT_LOADER = resultSet -> {
        try {
            String crateId = resultSet.getString(DataHandler.COLUMN_CRATE_ID.getName());
            String rewardId = resultSet.getString(DataHandler.COLUMN_REWARD_ID.getName());
            String holder = resultSet.getString(DataHandler.COLUMN_HOLDER.getName());

            int amount = resultSet.getInt(DataHandler.COLUMN_AMOUNT.getName());
            long resetDate = resultSet.getLong(DataHandler.COLUMN_RESET_DATE.getName());

            return new RewardLimit(crateId, rewardId, holder, amount, resetDate);
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    };


    public static final InsertQuery<GlobalCrateData> CRATE_DATA_INSERT = new InsertQuery<GlobalCrateData>()
        .setValue(DataHandler.COLUMN_CRATE_ID, GlobalCrateData::getCrateId)
        .setValue(DataHandler.COLUMN_LATEST_OPENER_ID, data -> data.getLatestOpenerId() == null ? "null" : data.getLatestOpenerId().toString())
        .setValue(DataHandler.COLUMN_LATEST_OPENER_NAME, data -> String.valueOf(data.getLatestOpenerName()))
        .setValue(DataHandler.COLUMN_LATEST_REWARD_ID, data -> String.valueOf(data.getLatestRewardId()));

    public static final UpdateQuery<GlobalCrateData> CRATE_DATA_UPDATE = new UpdateQuery<GlobalCrateData>()
        .whereIgnoreCase(DataHandler.COLUMN_CRATE_ID, WhereOperator.EQUAL, GlobalCrateData::getCrateId)
        .setValue(DataHandler.COLUMN_LATEST_OPENER_ID, data -> data.getLatestOpenerId() == null ? "null" : data.getLatestOpenerId().toString())
        .setValue(DataHandler.COLUMN_LATEST_OPENER_NAME, data -> String.valueOf(data.getLatestOpenerName()))
        .setValue(DataHandler.COLUMN_LATEST_REWARD_ID, data -> String.valueOf(data.getLatestRewardId()));

    public static final DeleteQuery<GlobalCrateData> CRATE_DATA_DELETE = new DeleteQuery<GlobalCrateData>()
        .whereIgnoreCase(DataHandler.COLUMN_CRATE_ID, WhereOperator.EQUAL, GlobalCrateData::getCrateId);

    public static final DeleteQuery<Crate> CRATE_DATA_DELETE_CRATE = new DeleteQuery<Crate>()
        .whereIgnoreCase(DataHandler.COLUMN_CRATE_ID, WhereOperator.EQUAL, Crate::getId);



    public static final InsertQuery<RewardLimit> REWARD_LIMIT_INSERT = new InsertQuery<RewardLimit>()
        .setValue(DataHandler.COLUMN_CRATE_ID, RewardLimit::getCrateId)
        .setValue(DataHandler.COLUMN_REWARD_ID, RewardLimit::getRewardId)
        .setValue(DataHandler.COLUMN_HOLDER, RewardLimit::getHolder)
        .setValue(DataHandler.COLUMN_AMOUNT, limit -> String.valueOf(limit.getAmount()))
        .setValue(DataHandler.COLUMN_RESET_DATE, limit -> String.valueOf(limit.getResetDate()));

    public static final UpdateQuery<RewardLimit> REWARD_LIMIT_UPDATE = new UpdateQuery<RewardLimit>()
        .whereIgnoreCase(DataHandler.COLUMN_HOLDER, WhereOperator.EQUAL, RewardLimit::getHolder)
        .whereIgnoreCase(DataHandler.COLUMN_CRATE_ID, WhereOperator.EQUAL, RewardLimit::getCrateId)
        .whereIgnoreCase(DataHandler.COLUMN_REWARD_ID, WhereOperator.EQUAL, RewardLimit::getRewardId)
        .setValue(DataHandler.COLUMN_AMOUNT, limit -> String.valueOf(limit.getAmount()))
        .setValue(DataHandler.COLUMN_RESET_DATE, limit -> String.valueOf(limit.getResetDate()));

    public static final DeleteQuery<RewardLimit> REWARD_LIMIT_DELETE = new DeleteQuery<RewardLimit>()
        .whereIgnoreCase(DataHandler.COLUMN_HOLDER, WhereOperator.EQUAL, RewardLimit::getHolder)
        .whereIgnoreCase(DataHandler.COLUMN_CRATE_ID, WhereOperator.EQUAL, RewardLimit::getCrateId)
        .whereIgnoreCase(DataHandler.COLUMN_REWARD_ID, WhereOperator.EQUAL, RewardLimit::getRewardId);

    public static final DeleteQuery<Crate> REWARD_LIMIT_DELETE_CRATE = new DeleteQuery<Crate>()
        .whereIgnoreCase(DataHandler.COLUMN_CRATE_ID, WhereOperator.EQUAL, Crate::getId);

    public static final DeleteQuery<Reward> REWARD_LIMIT_DELETE_REWARD = new DeleteQuery<Reward>()
        .whereIgnoreCase(DataHandler.COLUMN_CRATE_ID, WhereOperator.EQUAL, reward -> reward.getCrate().getId())
        .whereIgnoreCase(DataHandler.COLUMN_REWARD_ID, WhereOperator.EQUAL, Reward::getId);

    public static final DeleteQuery<UUID> REWARD_LIMIT_DELETE_PLAYER = new DeleteQuery<UUID>()
        .whereIgnoreCase(DataHandler.COLUMN_HOLDER, WhereOperator.EQUAL, UUID::toString);
}
