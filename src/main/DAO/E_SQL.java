package main.DAO;

public enum E_SQL {
  findAllContract("select * from contract"),
  insertContract("insert into contract (contract_id, contract_date, expiration_date, product_id, sales_id, state, customer_id) values (?,?,?,?,?,?,?)");
  private String sql;

  E_SQL(String sql) {
    this.sql = sql;
  }

  public String sql(){
      return sql;
  }

}
