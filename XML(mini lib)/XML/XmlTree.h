#ifndef __XMLTREE__
#define __XMLTREE__

#include <map>
#include <string>
#include <vector>
#include <stdexcept>


class XmlNode
{
public:
	XmlNode(const std::string& s) : element(s)
	{
		next_sib = nullptr;
		prev_sib = nullptr;
		parent = nullptr;
	}
	void takeAttribute(const std::string& property, int* x);
	void takeAttribute(const std::string& property, std::string* s);
	void takeText(std::string* s)
	{
		if (text.empty() == false)
		{
			*s = text;
		}
		else
		{
			throw std::runtime_error("No text");
		}
	}

	std::string element;
	std::map<std::string, std::string> attribute_set;
	std::vector<XmlNode*> child;
	XmlNode* parent;
	XmlNode* next_sib;
	XmlNode* prev_sib;

private:
	friend class XmlTree;
	std::string text;
};

class XmlTree
{
public:
	XmlTree()
	{
		mRoot = nullptr;
	}
	~XmlTree()
	{

	}

	void parseXmlFile(const char* file_name)
	{
		parse(file_name);
	}
	void parseXmlFile(const std::string& file_name)
	{
		parse(file_name.c_str());
	}
	XmlNode* getRoot()
	{
		return mRoot;
	}

private:
	void parse(const char* fileName);

	XmlNode* mRoot;
};

#endif

