

docker:
	sudo systemctl start docker

compose-build:
	sudo docker-compose build

compose-up:
	sudo docker-compose up

fe-format:
	cd todo-fe && dartfmt -w -l 160 --fix .

fe-get:
	echo "getting frontend dependencies" 
	cd todo-fe && pub get

fe-build: 
	echo "building frontend"
	cd todo-fe && pub global activate webdev
	cd todo-fe && pub run build_runner build --delete-conflicting-outputs

fe-run: 
	echo "running frontend"
	# cd todo-fe && webdev serve --hostname 0.0.0.0 --release
	cd todo-fe && pub run build_runner serve --hostname 0.0.0.0 --release

fe-all: fe-format fe-get fe-build fe-run


be-test: 
	echo "testing backend" 
	cd todo-be && gradle clean test 

be-build: 
	echo "building backend" 
	cd todo-be && gradle clean build
	
be-run: 
	echo "running backend"
	cd todo-be && java -jar build/libs/todo-0.0.1-SNAPSHOT.jar

be-linter:
	echo "running linter"
	cd todo-be && gradle ktlint

be-format:
	echo "formatting kotlin code"
	cd todo-be && gradle ktlintFormat

be-all: be-format be-linter be-test be-build be-run
