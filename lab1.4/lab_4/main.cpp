// main.cpp
// Lab Works #4

#include "pch.h"
#include <iostream>
#include <cstddef> // size_t
#include <vector>
#include <algorithm>
#include <string>
#include "main_class.h"

double const ER = 0.000000000001;

using namespace std;


EvalTable::EvalTable(size_t nRows, double fst, double lst)
	: Table(nRows, fst, lst), res_(0)
{
	ptable_ = new double *[nRows];
	// allocate memory
	ptable_[0] = new double[2 * nRows];
	// set pointers
	for (size_t i = 1; i != nRows; ++i)
		ptable_[i] = ptable_[i - 1] + 2;
}

EvalTable::~EvalTable()
{
	delete[] ptable_[0];
	delete[] ptable_;
}

void
EvalTable::fillTable()
{
	// evaluation step
	double h = (getLst() - getFst()) / (getRows() - 1);
	// fill the table
	for (size_t i = 0; i != getRows(); ++i)
	{
		double temp = getFst() + i * h;
		ptable_[i][0] = temp;
		ptable_[i][1] = std::sqrt(1 + temp * temp);//1 - std::exp(-2 * temp);
	}
}

void
EvalTable::printTable() const
{
	cout << endl << "-----TABLE-----" << endl;
	// find a cell with max length
	size_t maxsize = 0;
	for (size_t i = 0; i != getRows(); ++i)
	{
		string tempstr = to_string(ptable_[i][0]);
		if (tempstr.size() > maxsize)
			maxsize = tempstr.size() + 1;
		tempstr = to_string(ptable_[i][1]);
		if (tempstr.size() > maxsize)
			maxsize = tempstr.size() + 1;
	}

	// print table
	for (size_t i = 0; i < getRows(); ++i)
	{
		string buff = to_string(ptable_[i][0]);
		while (maxsize - buff.size()) buff += " ";
		cout << buff << " | ";
		buff = to_string(ptable_[i][1]);
		while (maxsize - buff.size()) buff += " ";
		cout << buff << " | ";
		cout << endl;
	}
}

void
EvalTable::sortTable(double x)
{
	vector<double> v;
	for (size_t i = 0; i < getRows(); ++i)
		v.push_back(ptable_[i][0]);
	std::sort(v.begin(), v.end(), Comparator(x));
	for (size_t i = 0; i < getRows(); ++i)
	{
		size_t k;
		for (size_t j = 0; j < getRows(); ++j)
			if (ptable_[j][0] == v[i]) k = j;
		std::swap(ptable_[i][0], ptable_[k][0]);
		std::swap(ptable_[i][1], ptable_[k][1]);
	}
}

double
EvalTable::evaluateByLagrange(size_t n, double x)
{
	res_ = 0;
	for (size_t i = 0; i < n + 1; ++i)
	{
		double temp = 1;
		for (size_t j = 0; j < n + 1; ++j)
		{
			if (j != i)
			{
				temp *=
					(x - ptable_[j][0])
					/ (ptable_[i][0] - ptable_[j][0]);
			}
		}
		res_ += ptable_[i][1] * temp;
	}

	return res_;
}

double
EvalTable::evaluateByBisection(size_t n, double x, double eps = 0.00000001)
{
	cout << "Calculating by bisection...\n";
	double beg = getFst();
	double end = getLst();
	double dx = 0.0;
	double res = 0.0;
	if (std::abs(evaluateByLagrange(n, beg) - x) < ER)
		return beg;
	if (std::abs(evaluateByLagrange(n, end) - x) < ER)
		return end;
	while (end - beg > eps) {
		dx = (end - beg) / 2;
		res = beg + dx;
		if ((evaluateByLagrange(n, beg) - x) *
			(evaluateByLagrange(n, res) - x) < 0)
			end = res;
		else
			beg = res;
	}
	res_ = res;
	return res_;
}

void
EvalTable::swapColomns() {
	for (size_t i = 0; i != getRows(); ++i)
		std::swap(ptable_[i][0], ptable_[i][1]);
}

double
EvalTable::getValueOfFirst() const { return ptable_[0][0]; }

double
EvalTable::getValueOfLast() const { return ptable_[getRows() - 1][0]; }

DerivedTable::DerivedTable(size_t nRows, double fst, double step)
	: Table(nRows, fst, fst + (nRows - 1) * step), step_(step)
{
	ptable_ = new double *[nRows];
	// allocate memory
	ptable_[0] = new double[6 * nRows];
	// set pointers
	for (size_t i = 1; i != nRows; ++i)
		ptable_[i] = ptable_[i - 1] + 6;
}

DerivedTable::~DerivedTable()
{
	delete[] ptable_[0];
	delete[] ptable_;
}

void
DerivedTable::fillTable()
{
	// fill the first and second colomns of table
	for (size_t i = 0; i != getRows(); ++i)
	{
		double temp = getFst() + i * step_;
		ptable_[i][0] = temp;
		ptable_[i][1] = std::exp(1.5 * temp);
	}
	// fill the other colomns of table
	ptable_[0][2] =
		(-3 * ptable_[0][1] +
			4 * ptable_[1][1] -
			ptable_[2][1]) /
			(2 * step_);
	ptable_[getRows() - 1][2] =
		(3 * ptable_[getRows() - 1][1] -
			4 * ptable_[getRows() - 2][1] +
			ptable_[getRows() - 3][1]) /
			(2 * step_);

	for (size_t j = 2; j != 6; ++j)
	{
		for (size_t i = 0; i < getRows(); ++i)
		{
			if ((j == 5 || j == 4) && (i == 0 || i == getRows() - 1)) 
				continue;

			if (i > 0 && i < getRows() - 1 && j == 2)
				ptable_[i][j] =
				(ptable_[i + 1][1] - ptable_[i - 1][1]) /
				(2 * step_);

			if (j == 4)
				ptable_[i][j] =
				(ptable_[i + 1][1] - 2 * ptable_[i][1] +
					ptable_[i - 1][1]) /
					(step_ * step_);

			if (j == 3)
				ptable_[i][j] =
					std::abs(1.5 * std::exp(1.5 * ptable_[i][0]) - ptable_[i][2]);

			if (j == 5)
				ptable_[i][j] =
					std::abs(2.25 * std::exp(1.5 * ptable_[i][0]) - ptable_[i][4]);
		}
	}
}

void
DerivedTable::printTable() const
{
	cout << endl << "-----TABLE-----" << endl;
	// find a cell with max length
	size_t maxsize = 0;
	for (size_t i = 0; i != getRows(); ++i)
	{
		string tempstr = to_string(ptable_[i][0]);
		if (tempstr.size() > maxsize)
			maxsize = tempstr.size() + 1;
		tempstr = to_string(ptable_[i][1]);
		if (tempstr.size() > maxsize)
			maxsize = tempstr.size() + 1;
	}
	// print table
	for (size_t i = 0; i < getRows(); ++i)
	{
		for (size_t j = 0; j < 6; ++j)
		{
			if ((j == 5 || j == 4) && (i == 0 || i == getRows() - 1))
				continue;
			string buff = to_string(ptable_[i][j]);
			while (maxsize - buff.size()) buff += " ";
			cout << buff << " | ";
		}
		cout << endl;
	}
}

int main()
{
	setlocale(LC_ALL, "rus");
	size_t m;
	size_t n;
	double a;
	double b;
	double x;

	cout << endl << "Enter a number of nodes in the table: ";
	cin >> m;

	cout << "Enter the left and right border of the segment : ";
	cin >> a >> b;

	EvalTable * rt = new EvalTable(m, a, b);

	rt->fillTable();
	rt->printTable();

	cout << endl << "Enter the degree of interpolation polynomial:\n";
	cout << "Note that n must not exceed m..." << m - 1 << "!\n";
	cin >> n;
	while (n > m - 1)
	{
		cout << endl << "Incorrect n!\nPlease, try again:";
		cin >> n;
	}

	rt->swapColomns();
	double x1 = std::min(rt->getValueOfFirst(), rt->getValueOfLast());
	double x2 = std::max(rt->getValueOfFirst(), rt->getValueOfLast());
	cout << "Enter a value for the inverse interpolation from the segment["
		<< x1 << ", " << x2 << "]: ";
	cin >> x;
	while (x1 - x > ER || x - x2 > ER)
	{
		cout << endl << "Incorrect x!\nPlease, try again:";
		cin >> x;
	}

	cout.precision(11);
	cout.fixed;
	if (n < m - 1) rt->sortTable(x);
	rt->printTable();

	// Task1
	cout << endl << "Calculating by Lagrange's technique...\n";
	double res = rt->evaluateByLagrange(n, x);
	cout << "Value: " << res << endl;

	cout << "Residual module: ";
	cout << std::abs(/*1 - std::exp(-2 * res)*/std::sqrt(1 + res * res) - x) << endl;

	// Task2
	rt->swapColomns();
	//if (n < m - 1) rt->sortTable(x);
	rt->printTable();

	res = rt->evaluateByBisection(n, x, 0.000000000001);
	cout << "Value: " << res << endl;

	cout << "Residual module: ";
	cout << std::abs(/*1 - std::exp(-2 * res)*/std::sqrt(1 + res * res) - x) << endl;

	// Task3
	double h = 1.0;
	double N = 0.0;
	cout << "Enter a count of nodes: ";
	cin >> N;
	cout << "Enter a step: ";
	cin >> h; cout << endl;
	cout << "Enter the first value: ";
	cin >> a; cout << endl;
	DerivedTable * qt = new DerivedTable(N, a, h);
	
	qt->fillTable();
	qt->printTable();

	int k = 0;
	cout << "Select an action...\n";
	cout << "0 - exit, 1 - enter new data...\n" << endl;
	cin >> k;
	if (k) main();

	delete qt;
	delete rt;
	return 0;
}

