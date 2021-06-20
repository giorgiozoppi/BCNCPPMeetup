#ifndef BASKET_H
#define BASKET_H
class basket {
    public:
    basket(int apples, int oranges, int pineapples) {
        _apples = apples;
        _oranges = oranges;
        _pineapples = pineapples;
    }
    private:
    int _apples{0};
    int _oranges{0};
    int _pineapples{0};
};
#endif

