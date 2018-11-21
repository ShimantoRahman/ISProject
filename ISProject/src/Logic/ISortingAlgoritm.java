package Logic;

import SysteemKlasses.*;

public interface ISortingAlgoritm {
    public Toewijzingsaanvraag[] sort(Toewijzingsaanvraag[] toewijzingsaanvragen, School school) throws ToewijzingsaanvraagException;
}
