package digikala;


public interface DigiKalaInterFace {
    void Start();
    void End();
    void s_addedMobileToMyBasket();
    void s_digikalaSite();
    void s_isLGTVSearched() throws InterruptedException;
    void s_isMobileSearched() throws InterruptedException;
    void s_isTVSearched() throws InterruptedException;
    void s_mobileDetailPage();
    void s_TVDetailPage();
    void t_close();
    void t_searchMobile();
    void t_TVSearch();
    void t_LGTVDetail() throws InterruptedException;
    void t_mobileDetail() throws InterruptedException;
    void t_LGTVSearch() throws InterruptedException;
    void t_addToBasket();
    void t_gotoHomePage();
    void t_gotoDigikalaSite();
    void s_emptyBasket();
    void t_removeMobileFromBasket();
    void t_backToDetail() throws InterruptedException;

}
