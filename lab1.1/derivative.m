% ������� �������� �������� ��������
%   ������ ����������� � �������� �����
% ��������� ������ 'a' ������������� ��������,
% �������� 'x', ��� ������� �����������
%   ������ ����������� ��������
% ���������� �������� 
%   ������ ����������� �������� ��� 'x'
function val = derivative(a,x)
% ������ ������� �������������
n = size(a,2);
b = zeros(1, n - 1);
% b(0) = a(0)
b(1) = a(1);
% ���������� ������������� ������ �����������
%   � ������� ����� �������
for i = 2:(n - 1)
    b(i) = b(i - 1) * x + a(i);
end
% ���������� ����� ������� � ������������� P'n(x) �
%    ���������� �������� 
val = polinom(b,x);