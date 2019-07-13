#ifndef __ICLASSES__
#define __ICLASSES__

class Comparator
{
public:
	explicit Comparator(double);

	bool operator()(const double&, const double&) const;

private:
	double val_;

};

class Table
{
public:
	Table(size_t, double, double);
	
	virtual ~Table();

	virtual void printTable() const = 0;

protected:
	size_t getRows() const;

	double getFst() const;

	double getLst() const;

private:
	size_t nRows_;
	double fst_;
	double lst_;

};

#endif __ICLASSES__

