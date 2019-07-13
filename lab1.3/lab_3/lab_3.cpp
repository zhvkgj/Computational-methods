// lab_3.cpp

#include "pch.h"
#include <iostream>
#include <cstddef> // size_t
#include <cmath>   // sin, abs
#include <algorithm>
#include <string>

double const ER = 0.00000000001;

using namespace std;

class MyTable
{
private:
	size_t row_;

	double a_;
	double b_;

public:
	MyTable(size_t m, double a, double b)
		: row_(m + 1), a_(a), b_(b)
	{}
	virtual ~MyTable() {}
	virtual void print_table() const = 0;

protected:
	size_t get_row() const { return row_; }

	double get_a() const { return a_; }

	double get_b() const { return b_; }
};

class Raw_Table : public MyTable
{
private:
	double ** ptable_;
	// non-copyable class
	Raw_Table (const Raw_Table&);
	Raw_Table & operator=(const Raw_Table&);
	
public:
	Raw_Table(size_t m, double a, double b)
		: MyTable(m, a, b)
	{

		ptable_ = new double *[m + 1];
		// allocate memory
		ptable_[0] = new double[2 * (m + 1)];
		// set pointers
		for (size_t i = 1; i != m + 1; ++i)
			ptable_[i] = ptable_[i - 1] + 2;
		// evaluation h
		double h = (b - a) / m;
		// fill the first and second colomns of table
		for (size_t i = 0; i != m + 1; ++i)
		{
			double temp = a + i * h;
			ptable_[i][0] = temp;
			ptable_[i][1] = std::sin(temp) + temp * temp / 2;
		}
	}
	
	~Raw_Table()
	{
		delete[] ptable_[0];
		delete[] ptable_;
	}

	void print_table() const
	{
		cout << endl << "-----TABLE-----" << endl;
		// find a cell with max length
		size_t maxsize = 0;
		for (size_t i = 0; i != get_row(); ++i)
		{
			string tempstr = to_string(ptable_[i][0]);
			if (tempstr.size() > maxsize)
				maxsize = tempstr.size() + 1;
			tempstr = to_string(ptable_[i][1]);
			if (tempstr.size() > maxsize)
				maxsize = tempstr.size() + 1;
		}

		// print title
		string title = " x";
		while (maxsize - title.size() + 1) title += " ";
		title += "|";
		cout << title;
		title = " f(x)";
		while (maxsize - title.size() + 2) title += " ";
		title += "|";
		cout << title << endl;

		// print table
		for (size_t i = 0; i < get_row(); ++i)
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

};

class Full_Table : public MyTable
{
private:	
	// degree of polinom
	size_t n_;

	// result of evaluation
	double res_;

	// non-copyable class
	Full_Table(const Full_Table&);
	Full_Table & operator=(const Full_Table&);

protected:
	double ** ptable_;

	size_t get_n() const { return n_; }

public:
	Full_Table(size_t m, double a,
			   double b, size_t n) 
		: MyTable(m, a, b), n_(n)
	{
		// allocate memory
		ptable_ = new double *[m + 1];
		ptable_[0] = new double[(2 + n_) * (m + 1)];
		// set pointers
		for (size_t i = 1; i != m + 1; ++i)
			ptable_[i] = ptable_[i - 1] + (2 + n_);
		// evaluation h
		double h = (b - a) / m;
		// fill the first and second colomns of table
		for (size_t i = 0; i != m + 1; ++i)
		{
			double temp = a + i * h;
			ptable_[i][0] = temp;
			ptable_[i][1] = std::sin(temp) + temp * temp / 2;
		}
		size_t k = m + 1;
		// fill the other colomns of table
		for (size_t i = 2; i < n_ + 2; ++i)
		{
			k--;
			for (size_t j = 0; j < k; ++j)
			{
				ptable_[j][i] = ptable_[j + 1][i - 1] - ptable_[j][i - 1];
			}
		}
	}

	virtual ~Full_Table()
	{
		delete[] ptable_[0];
		delete[] ptable_;
	}

	void print_table() const
	{
		cout << endl << "-----TABLE-----" << endl;
		// find a cell with max length
		size_t maxsize = 0;
		for (size_t i = 0; i != get_row(); ++i)
		{
			string tempstr = to_string(ptable_[i][0]);
			if (tempstr.size() > maxsize)
				maxsize = tempstr.size() + 1;
			tempstr = to_string(ptable_[i][1]);
			if (tempstr.size() > maxsize)
				maxsize = tempstr.size() + 1;
		}

		// print title
		string title = " x";
		while (maxsize - title.size() + 1) title += " ";
		title += "|";
		cout << title;
		title = " f(x)";
		while (maxsize - title.size() + 2) title += " ";
		title += "|";
		cout << title << endl;

		size_t k = n_;
		// print table
		for (size_t i = 0; i < get_row(); ++i)
		{
			if (i > get_row() - n_ - 1) k--;
			for (size_t j = 0; j < k + 2; ++j)
			{
				string buff = to_string(ptable_[i][j]);
				while (maxsize - buff.size()) buff += " ";
				cout << buff << " | ";
			}
			cout << endl;
		}
	}
	double evaluate(double x)
	{
		for (size_t i = 0; i < get_row(); ++i)
		{
			double temp = ptable_[i][0];
			if (std::abs(x - temp) < ER)
			{
				res_ = ptable_[i][1];
				return res_;
			}
		}

		double h = (get_b() - get_a()) / (get_row() - 1);

		// for begin
		if (x - get_row() > ER && (get_row() + h) - x > ER)
			res_ = eval_begin(x, h);

		// for middle
		if (x - (get_a() + h * ((n_ + 1) / 2)) > ER &&
			get_b() - h * ((n_ + 1) / 2) - x > ER ||
			get_a() + h * ((n_ + 1) / 2) - x < ER &&
			x - (get_b() - h * ((n_ + 1) / 2)) < ER)
			res_ = eval_middle(x, h);

		// for end
		if (x - (get_b() - h) > ER && get_b() - x > ER)
			res_ = eval_end(x, h);

		return res_;
	}
	
	double eval_begin(double x, double h) const
	{
		cout << endl << "Алгоритм начала таблицы...\n";
		double t = (x - ptable_[0][0]) / h;
		double result = ptable_[0][1];
		double temp = 1;
		for (size_t i = 2; i < n_ + 2; ++i)
		{
			temp *= (t - i + 2) / (i - 1);
			result += ptable_[0][i] * temp;
		}

		return result;
	}

	double eval_middle(double x, double h) const
	{
		cout << endl << "Алгоритм середины таблицы...\n";
		size_t idx = (n_ + 1) / 2;
		while (idx++)
		{
			if (x < ptable_[idx][0])
			{
				idx--;
				break;
			}
		}
		double t = (x - ptable_[idx][0]) / h;
		double result = ptable_[idx][1];
		double temp = 1;
		for (unsigned i = 2; i < n_ + 2; ++i)
		{
			temp *= (t + pow(-1, i - 2) * ((i - 1) / 2)) / (i - 1);
			result += ptable_[idx - (i - 1) / 2][i] * temp;
		}

		return result;
	}

	double eval_end(double x, double h) const
	{
		cout << endl << "Алгоритм конца таблицы...\n";
		double t = (x - ptable_[get_row() - 1][0]) / h;
		double result = ptable_[get_row() - 1][1];
		double temp = 1;
		for (size_t i = 2; i < n_ + 2; ++i)
		{
			temp *= (t + i - 2) / (i - 1);
			result += ptable_[get_row() - i][i] * temp;
		}

		return result;
	}

	double eval_error(double x)
	{
		return std::abs(std::sin(x) + x * x / 2 - res_);
	}
};

int main()
{
	setlocale(LC_ALL, "rus");
	size_t m;
	size_t n;
	double a;
	double b;
	double h;
	cout << "Введите два числа - начало и конец отрезка соответственно: ";
	cin >> a >> b;
	cout << endl << "Введите m - число частей, на которые делится отрезок: ";
	cin >> m;
	MyTable *eq1 = new Raw_Table(m, a, b);
	eq1->print_table();
	cout << endl << "Введите n - степень интерполяционного многочлена:\n";
	cout << "Обратите внимание, что n должно быть не больше " << m << "!\n";
	cin >> n;
	while (n > m)
	{
		cout << endl << "Некорректный n!\nПопробуйте снова:";
		cin >> n;
	}
	h = (b - a) / m;
	cout << "Введите x из промежутка: ";
	cout << "[ " << a << ", " << a + h << " ] или ";
	cout << "[ " << a + h * ((n + 1) / 2) << ", " << b - h * ((n + 1) / 2) << " ] или";
	cout << "[ " << b - h << ", " << b << " ]\n ";
	double x;
	cin >> x;
	while ((a - x) > ER || (x - (a + h)) > ER && 
			(a + h * ((n + 1) / 2)) - x > ER ||
			x - (b - h * ((n + 1) / 2)) > ER && 
			(b - h) - x > ER || (x - b) > ER)
	{
		cout << endl << "Некорректный x!\nПопробуйте снова:";
		cin >> x;
	}

	Full_Table * eq2 = new Full_Table(m, a, b, n);

	eq2->print_table();

	cout << "Значение Pn(" << x << "): ";
	cout << eq2->evaluate(x) << endl;

	cout << "Значение f(" << x << "): ";
	cout << std::sin(x) + x * x / 2 << endl;

	cout << "Значение погрешности | f(" << x << ") - Pn(" << x << ")|: ";
	cout << eq2->eval_error(x) << endl;

	int k = 0;
	cout << "Выберите дальнейшее действие\n";
	cout << "0 - завершить программу, 1 - ввести новые данные...\n" << endl;
	cin >> k;
	if (k) main();
	
	delete eq1;
	delete eq2;
	return 0;
}
