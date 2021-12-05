package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class PdtViewSvc { 
   public ProductInfo getPdtInfo(String id) {
      ProductInfo pdtInfo = null;
      Connection conn = getConnection();
      ProductDao productDao = ProductDao.getInstance();
      productDao.setConnection(conn); 
      pdtInfo = productDao.getPdtInfo(id); 

      return pdtInfo;
   }
}