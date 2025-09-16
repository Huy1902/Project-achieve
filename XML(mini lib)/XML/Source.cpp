#include <iostream>
#include "XmlTree.h"
using namespace std;

static void travel(XmlNode* parent)
{
	cout << "Element: " << parent->element << '\n';
	if (parent->attribute_set.size() > 0)
	{
		cout << "Attribute: " << '\n';
		for (const pair<string, string>& attribure : parent->attribute_set)
		{
			cout << attribure.first << ' ' << attribure.second << '\n';
		}
	}
	for (XmlNode* ite : parent->child)
	{
		cout << "\n\nGo to child :\n\n";
		travel(ite);
	}

	try
	{
		string text;
		parent->takeText(&text);
		cout << "\nText of element: " << text << '\n';
	}
	catch (const exception& error)
	{
		cout << error.what();
	}
}
int main()
{
	string s;
	cin >> s;
	XmlTree a;
	a.parseXmlFile(s);

	travel(a.getRoot());
}
