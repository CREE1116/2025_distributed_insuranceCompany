package main.Employee;

import java.util.*;
import java.sql.*;

import main.Enum.Sex;
import main.Data.InsuranceProduct;
import main.List.CustomerList;
import main.List.InsuranceProductList;

public class ProductManagement extends Employee {

	public ProductManagement(int numOfEmployees, EmployeeType employeeType, CustomerList customerList) {
		super(numOfEmployees, employeeType);
	}

	/**
	 * 보험 상품 생성
	 * 
	 * @param insuranceProducts
	 * @param coverageByAge
	 * @param exemptionPeriod
	 * @param reductionPeriod
	 * @param reductionRatio
	 * @param productName
	 * @param sex
	 * @param premium
	 * @param maxAge
	 * @param maxNumberEvent
	 * @return 생성 성공 시 true, 실패 시 false
	 */
	public boolean createProduct(InsuranceProductList insuranceProducts, HashMap<String, String> coverageByAge,
			int exemptionPeriod, int reductionPeriod, int reductionRatio, String productName, Sex sex, int premium,
			int maxAge, int maxNumberEvent) {
		InsuranceProduct product = null;
		if (!insuranceProducts.checkProduct(productName)) {
			product = new InsuranceProduct.InsuranceBuilder().coverageByAge(coverageByAge)
					.exemptionPeriod(exemptionPeriod).reductionPeriod(reductionPeriod).reductionRatio(reductionRatio)
					.productManagementID(getEmployeeID()).productName(productName).sex(sex).premium(premium)
					.maxAge(maxAge).maxNumberEvent(maxNumberEvent).build();
		}else{
			System.out.println("같은 이름의 상품이 있습니다.");
			return false;
		}
		product = insuranceProducts.insert(product);

		String sql = "INSERT INTO insurance_product (product_id, coverage_by_age, exemption_period, max_age, " +
				"max_number_event, premium, product_name, reduction_period, reduction_ratio, sex, product_management_id) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = dbConnection.con.prepareStatement(sql)) {
			pstmt.setString(1, product.getProductID());

			String coverageJson = toJson(product.getCoverageByAge());
			pstmt.setString(2, coverageJson);

			pstmt.setInt(3, product.getExemptionPeriod());
			pstmt.setInt(4, product.getMaxAge());
			pstmt.setInt(5, product.getMaxNumberEvent());
			pstmt.setInt(6, product.getPremium());
			pstmt.setString(7, product.getProductName());
			pstmt.setInt(8, product.getReductionPeriod());
			pstmt.setInt(9, product.getReductionRatio());
			pstmt.setString(10, product.getSex().name());
			pstmt.setString(11,product.getProductManagementID());

			pstmt.executeUpdate();
			System.out.println(product);
		}catch(SQLException e){
			System.out.println("에러 발생");
			System.out.println(e.getMessage());
			return false;
		}
		if(product!=null)
			return true;
		else
			return false;
	}

	/**
	 * 보험 상품 삭제
	 * 
	 * @param insuranceProductList
	 * @param productID
	 * @return delete 성공 시 true, 아닐 시 false
	 */
	public boolean deleteProduct(InsuranceProductList insuranceProductList, String productID) {
		String sql = "DELETE FROM insurance_product WHERE product_id = ?";
		try (PreparedStatement pstmt = dbConnection.con.prepareStatement(sql)) {
			pstmt.setString(1, productID);
			pstmt.executeUpdate();
		}catch(SQLException e){
			return false;
		}
		return insuranceProductList.delete(productID);
	}

	/**
	 * 보험 상품 수정
	 * 
	 * @param insuranceProductList
	 * @param product
	 * @return update 성공 시 true, 실패 시 false
	 */
	public boolean updateProduct(InsuranceProductList insuranceProductList, InsuranceProduct product) {
		String sql = "UPDATE insurance_product SET " +
				"coverage_by_age = ?, exemption_period = ?, max_age = ?, " +
				"max_number_event = ?, premium = ?, product_name = ?, " +
				"reduction_period = ?, reduction_ratio = ?, sex = ? " +
				"WHERE product_id = ?";

		try (PreparedStatement pstmt = dbConnection.con.prepareStatement(sql)) {
			pstmt.setString(1, toJson(product.getCoverageByAge())); // 수동 JSON 변환 함수 사용
			pstmt.setInt(2, product.getExemptionPeriod());
			pstmt.setInt(3, product.getMaxAge());
			pstmt.setInt(4, product.getMaxNumberEvent());
			pstmt.setInt(5, product.getPremium());
			pstmt.setString(6, product.getProductName());
			pstmt.setInt(7, product.getReductionPeriod());
			pstmt.setInt(8, product.getReductionRatio());
			pstmt.setString(9, product.getSex().name());
			pstmt.setString(10, product.getProductID());

			int rows = pstmt.executeUpdate();
			if(rows<=0) {
				System.out.println("문제가 발생해 업데이트가 되지 않았습니다.");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return insuranceProductList.update(product);
	}

	/**
	 * 상품 id로 조회
	 * 
	 * @param insuranceProductList
	 * @param productID
	 * @return
	 */
	public InsuranceProduct searchProduct(InsuranceProductList insuranceProductList, String productID) {
		return insuranceProductList.search(productID);
	}

	/**
	 * 보험 상품 키워드 조회 (productid, productname, productmanagementid)
	 * 
	 * @param insuranceProductList
	 * @param key
	 * @param value
	 * @return search 성공 시 true, 실패 시 false
	 */
	public InsuranceProductList searchProducts(InsuranceProductList insuranceProductList, String key, String value) {
		return insuranceProductList.searchProducts(key, value);
	}

	/**
	 * 보험 상품 리스트 중 index 이용해 조회
	 * 
	 * @param insuranceProductList
	 * @param index
	 * @return 사용자가 선택한 index값의 보험 상품 정보 반환
	 */
	public InsuranceProduct getProduct(InsuranceProductList insuranceProductList, int index) {
		return insuranceProductList.getProduct(index);
	}

	public static String toJson(HashMap<String, String> map) {
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{");

		int count = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey().replace("\"", "\\\"");   // 혹시 모를 이중 인용부호 방지
			String value = entry.getValue().replace("\"", "\\\"");
			jsonBuilder.append("\"").append(key).append("\":")
					.append("\"").append(value).append("\"");
			if (++count < map.size()) {
				jsonBuilder.append(",");
			}
		}

		jsonBuilder.append("}");
		return jsonBuilder.toString();
	}



}