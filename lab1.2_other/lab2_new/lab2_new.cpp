#include "pch.h"
#include <iostream>
#include <cstddef> // size_t
#include <cmath>   // sin, abs
#include <vector>
#include <algorithm>
#include <string>

double const ER = 0.000000000001;
double const pi = 3.14159;

using namespace std;

class Comparator
{
private:
	double val_;

public:
	explicit Comparator(double value)
		: val_(value)
	{}

	bool operator()(const double& lt, const double& rt) const
	{
		return (std::abs(lt - val_) < std::abs(rt - val_));
	}
};

class MyTable
{
private:
	size_t row_;

	double a_;
	double b_;
public:
	MyTable(size_t m, double a, double b)
		: row_(m), a_(a), b_(b)
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
	
	// result of evaluation
	double res_;
	// non-copyable class
	Raw_Table(const Raw_Table&);
	Raw_Table & operator=(const Raw_Table&);

public:
	Raw_Table(size_t m, double a, double b)
		: MyTable(m, a, b), res_(0)
	{
		ptable_ = new double *[m];
		// allocate memory
		ptable_[0] = new double[2 * m];
		// set pointers
		for (size_t i = 1; i != m; ++i)
			ptable_[i] = ptable_[i - 1] + 2;
	}

	~Raw_Table()
	{
		delete[] ptable_[0];
		delete[] ptable_;
	}

	void fill_step()
	{
		// evaluation step
		double h = (get_b() - get_a()) / (get_row() - 1);
		// fill the table
		for (size_t i = 0; i != get_row(); ++i)
		{
			double temp = get_a() + i * h;
			ptable_[i][0] = temp;
			ptable_[i][1] = 1 - std::exp(-2 * temp);
		}
	}

	void fill_hand()
	{
		vector<double> vec_;
		double temp = 0;
		for (size_t k = 0; k < get_row(); ++k)
		{
			cout << "Введите новую точку: " << endl;
			cin >> temp;
			while ((get_a() - temp > ER || temp - get_b() > ER) ||
				!(vec_.empty()) &&
				std::find(vec_.begin(), vec_.end(), temp) != vec_.end())
			{
				cout << "Такая точка уже есть в таблице "
					<< "или не входит в отрезок!" << endl
					<< "Введите новую точку: " << endl;
				cin >> temp;
			}
			vec_.push_back(temp);
			ptable_[k][0] = temp;
			ptable_[k][1] = 1 - std::exp(-2 * temp);
		}
	}

	void fill_cheb()
	{
		double t;
		for (size_t i = 0; i < get_row(); ++i)
		{
			t = std::cos((2 * i + 1) * pi / (2 * get_row()));
			ptable_[i][0] = ((get_b() - get_a()) * t + get_a() + get_b()) / 2;
			ptable_[i][1] = 1 - std::exp(-2 * ptable_[i][0]);
		}
	}

	void fill_table(int k)
	{
		switch (k)
		{
		case 0:
			fill_hand();
			break;
		case 1:
			fill_step();
			break;
		case 2:
			fill_cheb();
			break;
		default:
			fill_hand();
			break;
		}
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

	void sort_table(double x)
	{
		vector<double> v_;
		for (size_t i = 0; i < get_row(); ++i)
			v_.push_back(ptable_[i][0]);
		std::sort(v_.begin(), v_.end(), Comparator(x));
		for (size_t i = 0; i < get_row(); ++i)
		{
			size_t k;
			for (size_t j = 0; j < get_row(); ++j)
				if (ptable_[j][0] == v_[i]) k = j;
			std::swap(ptable_[i][0], ptable_[k][0]);
			std::swap(ptable_[i][1], ptable_[k][1]);
		}
	}

	double eval_newton(size_t n, double x)
	{
		double ** t_;
		// allocate memory
		t_ = new double *[n + 1];
		t_[0] = new double[(n + 2) * (n + 1)];
		// set pointers
		for (size_t i = 1; i < n + 1; ++i)
			t_[i] = t_[i - 1] + n + 2;
		// fill the table
		for (size_t i = 0; i < n + 1; ++i)
		{
			t_[i][0] = ptable_[i][0];
			t_[i][1] = ptable_[i][1];
		}

		size_t k = n + 1;
		for (size_t i = 2; i < n + 2; ++i)
		{
			k--;
			for (size_t j = 0; j < k; ++j)
			{
				t_[j][i] = 
					(t_[j + 1][i - 1] - t_[j][i - 1]) 
					 / (t_[j + i - 1][0] - t_[j][0]);
			}
		}

		cout << endl << "Вычисление с помощью "
			 << "интерполяционного многочлена Ньютона...\n";
		res_ = t_[0][1];
		double temp = 1;
		for (size_t i = 2; i < n + 2; ++i)
		{
			temp *= (x - t_[i - 2][0]);
			res_ += t_[0][i] * temp;
		}
		
		delete[] t_[0];
		delete[] t_;

		return res_;
	}

	double eval_lagrange(size_t n, double x)
	{
		cout << endl << "Вычисление с помощью "
			<< "интерполяционного многочлена Лагранжа...\n";
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

	void evaluate(size_t n, double x)
	{
		cout << eval_newton(n, x) << endl;
		cout << eval_error(x) << endl;
		cout << eval_lagrange(n, x) << endl;
		cout << eval_error(x) << endl;
	}

	double eval_error(double x)
	{
		cout << "Погрешность: ";
		double temp = std::abs(1 - std::exp(-2 * x) - res_);
		if (temp < ER) return 0;
		else return temp;
	}
};

int main()
{
	setlocale(LC_ALL, "rus");
	size_t m;
	size_t n;
	double a;
	double b;
	double x;
	int case_ = 0;

	cout << endl << "Введите m - число узлов в таблице: ";
	cin >> m;

	cout << "Введите два числа - начало и конец отрезка соответственно: ";
	cin >> a >> b;
	
	Raw_Table * rt = new Raw_Table(m, a, b);

	cout << "Выберите способ задания узлов...\n"
		 << "0 - случайные с клавиатуры, 1 - равноотстоящие"
		 << ", 2 - заданние многочленом Чебышёва...\n";
	cin >> case_;

	rt->fill_table(case_);
	rt->print_table();

	cout << endl << "Введите n - степень интерполяционного многочлена:\n";
	cout << "Обратите внимание, что n должно быть не больше " << m - 1 << "!\n";
	cin >> n;
	while (n > m - 1)
	{
		cout << endl << "Некорректный n!\nПопробуйте снова:";
		cin >> n;
	}

	cout << "Введите точку интерполяции из отрезка [" 
		 << a << ", " << b << "]: ";
	cin >> x;
	while (a - x > ER || x - b > ER)
	{
		cout << endl << "Некорректный x!\nПопробуйте снова:";
		cin >> x;
	}
	
	cout.precision(11);
	cout.fixed;
	rt->sort_table(x);
	rt->print_table();

	rt->evaluate(n, x);

	int k = 0;
	cout << "Выберите дальнейшее действие\n";
	cout << "0 - завершить программу, 1 - ввести новые данные...\n" << endl;
	cin >> k;
	if (k) main();

	delete rt;
	return 0;
}
