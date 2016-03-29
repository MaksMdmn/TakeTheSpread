package go.takethespread;

import java.util.List;

public interface OrderDao extends GenericDao<Order> {
    public List<Order>readByTicker(String ticker);
}
