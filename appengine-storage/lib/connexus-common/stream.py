

class stream:
    def __init__(self, streamID, name, owner, tags, coverImageURL):
        self.id = streamID
        self.name = name
        self.owner = owner
        self.tags = tags
        self.coverImage = coverImageURL
        self.lastPicDate = None
        self.images = []

    def add_image(self, imageURL ):
        self.images.append(imageURL)

    def delete_image(self, imageURL ):
        self.images.remove(imageURL)
