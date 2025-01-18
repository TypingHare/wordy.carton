burrow_root := "~/.burrow"

build:
    gradle clean
    gradle jar --quiet
    mkdir -p {{ burrow_root }}/cartons
    mv build/libs/* {{ burrow_root }}/libs/
