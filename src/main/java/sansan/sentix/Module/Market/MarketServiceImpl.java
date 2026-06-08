package sansan.sentix.Module.Market;

import org.springframework.stereotype.Service;
import sansan.sentix.Module.Market.Repository.SectorRepository;

import javax.annotation.Resource;
import java.sql.Clob;

@Service
public class MarketServiceImpl implements MarketService {

    @Resource
    private SectorRepository sectorRepository;
    @Override
    public Clob concatAllIndustryCodes() {
        return sectorRepository.concatAllIndustryCodes();
    }
}
