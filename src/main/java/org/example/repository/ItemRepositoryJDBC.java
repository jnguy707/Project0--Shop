package org.example.repository;

import org.example.db.DatabaseConnectionFactory;
import org.example.model.Customer;
import org.example.model.Item;
import org.example.model.Offer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ItemRepositoryJDBC implements ItemRepository{

    private static final Logger logger = Logger.getLogger("shop0");

    @Override
    public List<Item> findCatalog() {

        logger.info("querying database for catalog");

        List<Item> itemList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step-3 :  create JDBC statements with SQL
            String sql = "SELECT * " +
                    "FROM items ";
            PreparedStatement ps = connection.prepareStatement(sql);

            // step-4 :  execute JDBC-statements & process results
            ResultSet rs = ps.executeQuery();
            // Process Resultant Set
            while(rs.next()){
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setAvailable(rs.getBoolean("status"));
                itemList.add(item);
            }

            // step-5 : Handle SQL-exceptions
        } catch (SQLException e) {
            logger.warning(e.getMessage()); // print exception details in console
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
        return itemList;
    }

    @Override
    public List<Item> findItemsByID(Customer customer) {
        logger.info("querying database for customer's (#" + customer.getCustomerID() + ") items");

        List<Item> ownedItemList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step-3 :  create JDBC statements with SQL
            String sql = "SELECT id, name" +
                    "FROM items " +
                    "WHERE customer_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,customer.getCustomerID());

            // step-4 :  execute JDBC-statements & process results
            ResultSet rs = ps.executeQuery();
            // Process Resultant Set
            while(rs.next()){
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                ownedItemList.add(item);
            }

            // step-5 : Handle SQL-exceptions
        } catch (SQLException e) {
            logger.warning(e.getMessage()); // print exception details in console
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
        return ownedItemList;
    }

    @Override
    public double getDebit(Item item, Customer customer) {
        double initialDebit = 0;
        double standingCredit = 0;
        Connection connection = null;

        logger.info("accessing customer's account - " + customer.getCustomerID());
        logger.info("for debit of item #" + item.getId());
        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step-3 :  create JDBC statements with SQL
            String sql = "SELECT initialDebit, standingCredit " +
                    "FROM payables " +
                    "WHERE item_id = ? AND customer_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,item.getId());
            ps.setInt(2,customer.getCustomerID());

            // step-4 :  execute JDBC-statements & process results
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                logger.warning("No debit found for item #" + item.getId());
            }
            // Process Resultant Set
            while(rs.next()){
                initialDebit = rs.getDouble("initialDebit");
                standingCredit = rs.getDouble("standingCredit");
            }
            // step-5 : Handle SQL-exceptions
        } catch (SQLException e) {
            logger.warning(e.getMessage()); // print exception details in console
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warning(e.getMessage());
                }
            }
        }

        return initialDebit - standingCredit;
    }

    @Override
    public void createOffer(Item item, Customer customer, double amount) {
        Connection connection = null;
        try {

            logger.info("attempting to insert offer of " + amount +
                    "for item #" + item.getId() +
                    " and customer #" + customer.getCustomerID());

            connection = DatabaseConnectionFactory.getConnection();

            // step-3 :  create JDBC statements with SQL
            String sql = "INSERT INTO offers(amount, item_id, customer_id) " +
                    "VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1,amount);
            ps.setInt(2,item.getId());
            ps.setInt(3,customer.getCustomerID());

            // step-4 :  execute JDBC-statements & process results
            int rowCount = ps.executeUpdate();
            if (rowCount == 1)
                logger.info("offer successfully inserted");

            // step-5 : Handle SQL-exceptions
        } catch (SQLException e) {
            logger.warning(e.getMessage()); // print exception details in console
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    @Override
    public void createPayment(Item item, Customer customer, double amount){
        logger.info("attempting to make payment of " + amount
                + " for item#" + item.getId()
                + " by customer #" + customer.getCustomerID());
        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step-3 :  create JDBC statements with SQL
            // LEFT OUTER JOIN FROM items WITH offers
            String insertU = "UPDATE payables " +
                    "SET standingCredit = standingCredit + ? " +
                    "WHERE item_id = ? AND customer_id = ?";
            PreparedStatement ps = connection.prepareStatement(insertU);
            ps.setDouble(1,amount);
            ps.setInt(2, item.getId());
            ps.setInt(3, customer.getCustomerID());

            // step-4 :  execute JDBC-statements & process results
            int rowCount = ps.executeUpdate();
            if (rowCount == 1)
                logger.info("payment registered");
            // step-5 : Handle SQL-exceptions
        } catch (SQLException e) {
            logger.warning(e.getMessage()); // print exception details in console
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    @Override
    public List<Offer> findOffers(Item item) {

        List<Offer> offerList = new ArrayList<>();
        Connection connection = null;

        logger.info("querying database for offers on item #" + item.getId());

        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step-3 :  create JDBC statements with SQL
            String sql = "SELECT offers.amount, offers.customer_id " +
                    "FROM items INNER JOIN offers " +
                    "WHERE items.id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,item.getId());

            // step-4 :  execute JDBC-statements & process results
            ResultSet rs = ps.executeQuery();
            // Process Resultant Set
            while(rs.next()){
                Offer offer = new Offer();
                offer.setOfferID(rs.getInt("id"));
                offer.setOfferPrice(rs.getDouble("amount"));
                offer.setByCustomerID(rs.getInt("customer_id"));
                offerList.add(offer);
            }

            // step-5 : Handle SQL-exceptions
        } catch (SQLException e) {
            logger.warning(e.getMessage()); // print exception details in console
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
        return offerList;
    }

    @Override
    public void acceptOffer(Item item, Offer offer) {
        Connection connection = null;
        logger.info("attempting to finalize offer#"+ offer.getOfferID()
                + " for item#" + item.getId());


        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step-3 :  create JDBC statements with SQL
            // adding candidate offer to payables table
            String insertQ = "INSERT INTO payables " +
                    "(initialDebit, item_id, customer_id) " +
                    "SELECT amount, item_id, customer_id " +
                    "FROM offers " +
                    "WHERE id = ?";
            PreparedStatement psInsert = connection.prepareStatement(insertQ);
            psInsert.setInt(1,offer.getOfferID());

            // step-4 :  execute JDBC-statements & process results
            int rowCount = psInsert.executeUpdate();
            if (rowCount == 1)
                logger.info("offer #"+ offer.getOfferID()
                        + " has been assigned to payables");

            // Delete Query
            logger.info("attempting to reject all other offers " +
                    "on item# " + item.getId());

            String deleteQ = "DELETE FROM offers WHERE item_id = ?";

            PreparedStatement psDelete = connection.prepareStatement(deleteQ);
            psDelete.setInt(1,item.getId());

            rowCount = psDelete.executeUpdate();
            if (rowCount == 1)
                logger.info("rejected all other offers on item #" + item.getId());

            // step-5 : Handle SQL-exceptions
        } catch (SQLException e) {
            logger.warning(e.getMessage());

        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    @Override
    public void createItem(Item item) {

        Connection connection = null;

        logger.info("attempting to create item " + item.getName());

        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step-3 :  create JDBC statements with SQL
            // LEFT OUTER JOIN FROM items WITH offers
            String insertQ = "INSERT INTO items (name, status) " +
                    "VALUES (?, 1);";
            PreparedStatement ps = connection.prepareStatement(insertQ);
            ps.setString(1,item.getName());

            // step-4 :  execute JDBC-statements & process results
            int rowCount = ps.executeUpdate();
            if (rowCount == 1)
                logger.info("item " + item.getName() + " inserted into database");
            // step-5 : Handle SQL-exceptions
        } catch (SQLException e) {
            logger.warning(e.getMessage()); // print exception details in console
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    @Override
    public void updateItem(Item item){

        Connection connection = null;

        logger.info("attempting to update item #" + item.getId() + " to be named " + item.getName());

        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step-3 :  create JDBC statements with SQL
            // LEFT OUTER JOIN FROM items WITH offers
            String insertQ = "UPDATE items " +
                    "SET name = ?" +
                    "WHERE id = ?;";
            PreparedStatement ps = connection.prepareStatement(insertQ);
            ps.setString(1,item.getName());
            ps.setInt(2,item.getId());

            // step-4 :  execute JDBC-statements & process results
            int rowCount = ps.executeUpdate();
            if (rowCount == 1)
                logger.info("successfully updated item#" + item.getId());

            // step-5 : Handle SQL-exceptions
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e){
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    @Override
    public void deleteItem(Item item){
        // Input item is to be pushed to DB

        Connection connection = null;

        logger.info("attempting to delete item #" + item.getId());

        try {
            connection = DatabaseConnectionFactory.getConnection();
            // step-3 :  create JDBC statements with SQL
            // LEFT OUTER JOIN FROM items WITH offers
            String insertQ = "DELETE FROM items " +
                    "WHERE id = ?;";
            PreparedStatement ps = connection.prepareStatement(insertQ);
            ps.setInt(1,item.getId());

            // step-4 :  execute JDBC-statements & process results
            int rowCount = ps.executeUpdate();
            if (rowCount == 1)
                logger.info("successfully deleted item #" + item.getId());

        } catch (SQLException e) {
            logger.warning(e.getMessage());
        } finally {
            // step-7 : close / release connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }
}
