#ifndef __MAIN_H__
#define __MAIN_H__
#include "service_classes.h"

class EvalTable : public Table
{
public:
	EvalTable(size_t, double, double);

	~EvalTable();

	void fillTable();

	void printTable() const;

	void sortTable(double);

	double evaluateByLagrange(size_t, double);

	double evaluateByBisection(size_t, double, double);

	void swapColomns();

	double getValueOfFirst() const;

	double getValueOfLast() const;

private:
	double ** ptable_;
	double	  res_;

	EvalTable(const EvalTable&);
	EvalTable & operator=(const EvalTable&);

};

class DerivedTable : public Table
{
public:
	DerivedTable(size_t, double, double);

	~DerivedTable();

	void fillTable();

	void printTable() const;

private:
	double ** ptable_;
	double	  step_;

	DerivedTable(const EvalTable&);
	DerivedTable & operator=(const EvalTable&);

};

#endif __MAIN_H__

