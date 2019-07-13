// service.cpp

#include "pch.h"
#include <cmath>	// sin, abs
#include "service_classes.h"

Comparator::Comparator(double val)
	: val_(val)
{}

bool
Comparator::operator()(const double& lt, const double& rt) const
{
	return (std::abs(lt - val_) < std::abs(rt - val_));
}


Table::Table(size_t nRows, double fst, double lst)
	: nRows_(nRows), fst_(fst), lst_(lst)
{}

Table::~Table() {}

size_t
Table::getRows() const { return nRows_; }

double
Table::getFst() const { return fst_; }

double
Table::getLst() const { return lst_; }

