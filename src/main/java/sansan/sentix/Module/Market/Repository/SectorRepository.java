package sansan.sentix.Module.Market.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sansan.sentix.Module.Market.Entity.SectorEntity;

import java.sql.Clob;

@Repository
public interface SectorRepository extends JpaRepository<SectorEntity, Long> {
    @Query(value = "SELECT RTRIM(\n" +
            "         XMLAGG(\n" +
            "             XMLELEMENT(E, INDUSTRY_CODE || ';')\n" +
            "             ORDER BY ID\n" +
            "         ).EXTRACT('//text()').GETCLOBVAL(),\n" +
            "         ';'\n" +
            "       ) AS ST_SECTOR\n" +
            "FROM ST_SECTORS", nativeQuery = true)
    Clob concatAllIndustryCodes();

}
