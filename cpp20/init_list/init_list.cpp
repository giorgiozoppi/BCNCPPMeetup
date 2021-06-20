#include <cassert>
#include <initializer_list>
#include <iostream>
using std::cout;

struct date {
    date(std::initializer_list<int> values) {
        auto pos = values.begin();
        day = *pos;
        pos++;
        month = *pos;
        pos++;
        year = *pos;
    }
    int day;
    int month;
    int year;
};

int main() {
    date d  {31, 12, 2000};
    cout << d.day << " "<< d.month << " " << d.year << "\n";

}