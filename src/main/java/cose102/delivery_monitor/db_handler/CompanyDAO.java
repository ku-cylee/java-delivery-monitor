package cose102.delivery_monitor.db_handler;

import cose102.delivery_monitor.models.Company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyDAO extends DAO {
    public CompanyDAO() throws SQLException {
        super();
    }

    public void initialize() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS company (\n" +
                     "id INTEGER PRIMARY KEY,\n" +
                     "code VARCHAR(5),\n" +
                     "company VARCHAR(16)\n" +
                     ");";

        connection.createStatement().execute(sql);
    }

    public void insertCompanies(ArrayList<Company> companyList) throws SQLException {
        String sql = "INSERT INTO company (code, company) VALUES (?, ?)";

        for (Company company:companyList) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1 ,company.getCompanyCode());
            pstmt.setString(2, company.getCompanyName());
            pstmt.executeUpdate();
        }
    }

    public Company getCompany(int companyId) throws SQLException {
        Company company = null;

        String sql = "SELECT * FROM company WHERE company_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, companyId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) company = new Company(rs);

        return company;
    }

    public Company getCompany(String companyCode) throws SQLException {
        Company company = null;

        String sql = "SELECT * FROM company WHERE code = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, companyCode);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) company = new Company(rs);

        return company;
    }

    public ArrayList<Company> getCompanyList() throws SQLException {
        ArrayList<Company> companyList = new ArrayList<>();

        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM company");
        while (rs.next()) companyList.add(new Company(rs));

        return companyList;
    }
}
