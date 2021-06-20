#ifndef FARMER_H
#define FARMER_H
#include <string>
#include <memory>
#include <basket.hpp>
class farmer {
  // rule of five or rule of zero.
public:
  farmer(std::unique_ptr<basket> b, std::string n): _basket(std::move(b)), _name(n) 
  {} 
  farmer(farmer&& w) noexcept : _basket(std::move(w._basket)),
		      _name(std::move(w._name)), _vegetable(std::move(w._vegetable))
  {
  }
  // it doesn't make sense.
  farmer(farmer& w) noexcept : _basket(std::move(w._basket)),
		      _name(w._name), _vegetable(w._vegetable)
  {
  }
  farmer& operator=(farmer&& f) noexcept {
    _basket = std::move(f._basket);
    _name = std::move(f._name);
    _vegetable = std::move(f._vegetable);
    return *this;
  }
  farmer& operator=(farmer& f) noexcept {
    if (this!=&f) {
      _basket = std::move(f._basket);
      _name = f._name;
      _vegetable = f._vegetable;
    }
    return *this;
  }
  void harvest(int i) {
    for (auto j = 0; j < i; j++) {
      _vegetable.emplace_back(j);
    }
  }
private:
  std::unique_ptr<basket> _basket;
  std::string _name{};
  std::vector<double> _vegetable;
};
#endif
