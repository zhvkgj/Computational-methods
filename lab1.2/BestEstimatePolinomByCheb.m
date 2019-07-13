% ������� ������ �������� ���������� �����������
%   ������� f = x^n � ������� �������� ��������
% ���������� ������� Pn-1,
%   ����������� ��������� �����������,
%   ������ �������� �������� �������,
%   ������ �������� �������� ��������
function [p_arr, f_arr, T_arr] = BestEstimatePolinomByCheb()
% �������� ��������
x = -1:0.01:1;
% ������ �������� �������� �������
%   �� ��������� [-1, 1]
f_arr = zeros(1, 201);
% ������ �������� �������� ��������
%   �� �������� ���������
T_arr = zeros(1, 201);
% ������ �������� �������� �����������
p_arr = zeros(1, 201);
% ������� �������� ����������� �������
T = @(x)(32*x^6 - 48*x^4 + 18*x^2 - 1);
% ���������� �������� ������� f_arr,
%   ������� �������� �������� ��������,
%   ������� �������� �������� �����������
for i = 1:201
    f_arr(i) = x(i) ^ 6;
    T_arr(i) = T(x(i));
    p_arr(i) = f_arr(i) - (T_arr(i) / (2^(5)));
end

    

